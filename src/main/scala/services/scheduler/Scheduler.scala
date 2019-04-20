package services.scheduler

import java.time.temporal.ChronoUnit
import java.time.{OffsetDateTime, OffsetTime, ZoneOffset}

import entities.locations.Room
import entities.module.{Module, RequiredSession}
import entities.timing.TimePeriod
import services.parser.dsl.FilterList
import services.scheduler.poso.{Period, ScheduledClass}

import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, ListBuffer}
import scala.util.Random

object Scheduler {
  val filters: Seq[(Seq[ScheduleInterfaceMapper], Seq[ScheduleInterfaceMapper]) => Seq[ScheduleInterfaceMapper]] = FilterList.getFilters()

  def getPeriodDefault(dayOfMonth: Int) = getPeriod(dayOfMonth, 1, 8, 20)

  def getPeriod(dayOfMonth: Int, monthOfYear: Int, beginHour24: Int, endHour24: Int) =
    new Period(OffsetDateTime.parse(s"2019-0$monthOfYear-0${dayOfMonth}T00:00:00+00:00"), new TimePeriod {
      start = OffsetTime.of(beginHour24, 0, 0, 0, ZoneOffset.UTC)
      end = OffsetTime.of(endHour24, 0, 0, 0, ZoneOffset.UTC)
    })

  def binPackSchedule(daysToGenerate: Int, rooms: ArrayBuffer[Room], modules: Set[Module],
                      weights: Option[Seq[(Seq[ScheduleInterfaceMapper], ScheduleInterfaceMapper) => Double]] =
                      None): Option[List[ScheduledClass]] = {
    // These values are an estimates
    val term1End = 12
    val startDay = getPeriod(7, 1, 0, 0)

    val schedule = ListBuffer[ScheduledClass]()

    val fmap = modules.map(m => m.sessionStructure.map(_.session -> m)).flatten.toMap

    // group modules into Seq(Term)[Map[Session, Seq(Weeks session runs)[Int]]]
    modules.flatMap(_.sessionStructure.map(s => s.session -> s.weekNo)).groupBy(_._2 <= term1End).map(_._2.groupBy(_._1).map { case (k, v) => (k, v.map(_._2)) }.map(e => e._1 -> e._2.toList.sorted)).foreach(t => {
      // For each term

      // Build up groups of slots
      /* TODO:
          look into amending this to fix potential problems with conflicting constraints, making slots unschedulable
        */
      var slots = ListBuffer[Map[Int, RequiredSession]]()
      // Starting with the longest sessions
      t.toSeq.sortBy(-_._1.durationInHours).foreach(e => {
        // Find the most full slot teh session can fit in to
        val options = slots.filter(l => e._2.forall(i => !l.contains(i)))
        var choice: Map[Int, RequiredSession] = Map()
        // If such a slot exists, add the session to the slot
        // Otherwise create a new slot
        if (!options.isEmpty) {
          choice = options.maxBy(_.size)
          slots -= choice
        }
        e._2.foreach(i => choice += (i -> e._1))
        slots += choice
      })

      // Get schedule for term
      val termSchedule = binPackScheduleI(daysToGenerate, rooms,
        slots.map(s => new Event(s.map(_._2.durationInHours).max, s)).toSet,
        weights)

      // unpack schedule
      if (termSchedule.isDefined) {
        termSchedule.get.foreach(s => s.events.foreach(eo => eo._2.events.foreach(e => {
          val daysToAdd = (7 * e._1) + s.period.calendar.getDayOfWeek.getValue
          schedule += new ScheduledClass(new Period(startDay.calendar.plusDays(daysToAdd), startDay.timePeriod), eo._1, s.room, fmap(e._2).moduleName)
        }
        )))

      } else {
        return None
      }

    })

    Some(schedule.toList)
  }


  def binPackScheduleI(daysToGenerate: Int, rooms: ArrayBuffer[Room], events: Set[Event],
                       weights: Option[Seq[(Seq[ScheduleInterfaceMapper], ScheduleInterfaceMapper) => Double]] =
                       None
                      ): Option[List[RoomSchedule]] = {
    val periods = for (i <- 1 until daysToGenerate + 1) yield getPeriodDefault(i)

    val schedule = rooms.flatMap(r => periods.map(new RoomSchedule(r, _)))
    val wrappedSchedules: ListBuffer[ScheduleInterfaceMapper] = ListBuffer()

    // Events that have not been scheduled, shuffled to help distribute across the week
    var unProcEvents = Random.shuffle(events).to[ListBuffer]
    // Room schedules mapped to whether they contain space
    var scheduleMap = schedule.map((_ -> true)).toMap


    while (!unProcEvents.isEmpty) {
      // get the unfilled rooms
      val free = scheduleMap.filter(_._2).map(_._1).groupBy(_.period.calendar)

      if (free.isEmpty) {
        // If there are no free rooms, but there are events to be scheduled
        // return None, as failed to generate a schedule
        return None
      } else {
        // Get the free room that has the earliest unscheduled slot (day is included)
        val mostFree = free.minBy(_._1)._2.maxBy(_.timeRemaining)

        // Wrap scheduled and unscheduled events into a the standardised ScheduleInterfaceMapper
        // Apply the filters to the unscheduled events, to find those that can be scheduled in the slot
        var validEventsWrapped = filters.foldLeft(unProcEvents.map(new ScheduleInterfaceMapper(mostFree, _))) { (r, f) => f(wrappedSchedules, r).to[ListBuffer] }

        if (validEventsWrapped.isEmpty) {
          // no valid events can go in that slot
          /*TODO:
              Consider an approach that will assign free time, allowing the room to be scheduled later
              This will help prevent possible issues, where an early slot in a room is unscheduleable,
              which blocks the room being used later.
              e.g. It may be the case that the there are no events that can fit in a room from 11-12, but they can fit in 12-1,
               presently, this will prevent any slots after 11 from being scheduled
           */
          scheduleMap += (mostFree -> false)
        } else {
          // Apply the weighting functions
          if (weights.isDefined)
            validEventsWrapped = validEventsWrapped.groupBy(s => weights.get.map(_ (wrappedSchedules, s)).sum).maxBy(_._1)._2
          // Get the longest events
          validEventsWrapped = validEventsWrapped.groupBy(_.event.duration).maxBy(_._1)._2

          // Schedule the event
          mostFree + validEventsWrapped(0).event
          unProcEvents -= validEventsWrapped(0).event
          wrappedSchedules += validEventsWrapped(0)
        }

      }
    }

    Some(schedule.toList)
  }
}

class RoomSchedule(val room: Room, val period: Period) {
  var timeRemaining = (period.timePeriod.duration() / 60 / 60 / 1000).toFloat
  var durationPointer = period.timePeriod.start
  var events = mutable.HashMap[TimePeriod, Event]()

  def +(event: Event): Unit = {
    events += new TimePeriod(durationPointer, durationPointer.plus(event.duration.toLong, ChronoUnit.HOURS)) -> event
    timeRemaining -= event.duration
    durationPointer = durationPointer.plus(event.duration.toLong, ChronoUnit.HOURS)
    if(timeRemaining<0){
      throw new Exception("Cannot schedule event: event overlaps room")
    }
  }

}

class Event(val duration: Float, val events: Map[Int, RequiredSession])
