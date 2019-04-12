package services.scheduler

import java.time.temporal.ChronoUnit
import java.time.{OffsetDateTime, OffsetTime, ZoneOffset}

import entities.locations.Room
import entities.module.{Module, ModuleFehqLevel, RequiredSession}
import entities.timing.TimePeriod
import services.scheduler.Scheduler.ScheduledSession
import services.scheduler.poso.{Period, ScheduledClass}

import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, ListBuffer}
import scala.util.Random

object Scheduler {
  def getPeriodDefault(dayOfMonth: Int) = getPeriod(dayOfMonth, 1, 8, 20)

  def getPeriod(dayOfMonth: Int, monthOfYear: Int, beginHour24: Int, endHour24: Int) =
    new Period(OffsetDateTime.parse(s"2019-0$monthOfYear-0${dayOfMonth}T00:00:00+00:00"), new TimePeriod {
      start = OffsetTime.of(beginHour24, 0, 0, 0, ZoneOffset.UTC)
      end = OffsetTime.of(endHour24, 0, 0, 0, ZoneOffset.UTC)
    })

  def binPackSchedule(daysToGenerate: Int, rooms: ArrayBuffer[Room], modules: Set[ModuleFehqLevel]): Option[List[ScheduledClass]] ={
    binPackSchedule(daysToGenerate, rooms, modules, Array(willFit(_,_,_)))
  }

  def binPackSchedule(daysToGenerate: Int, rooms: ArrayBuffer[Room], modules: Set[ModuleFehqLevel],
                      filters: Seq[(Seq[RoomSchedule], Seq[Event], RoomSchedule) => Seq[Event]],
                      weights: Option[Seq[(Seq[RoomSchedule], Event, RoomSchedule) => Double]] =
                      None): Option[List[ScheduledClass]] ={
    // These values are an estimates
    val term1End = 12
    val startDay = getPeriod(7, 1, 0, 0)

    // map required sessions to weeks occured
    val classes = modules.flatMap(_.sessionStructure.groupBy(_.session))

    val classTimes = classes.map(c => c._1 -> c._2.map(_.weekNo).sorted.toList)

    // split into terms (Really inelegant)
    val terms = classTimes.map(c => c._2.map(c._1 -> _)).flatten.groupBy(_._2<= term1End).map(_._2.groupBy(_._1).map { case (k,v) => (k,v.map(_._2))}.map(e => e._1 -> e._2.toList.sorted))

    val schedule = ListBuffer[ScheduledSession]()

    terms.foreach(t => {
      // build up groups of slots
      // needs to be ammended to fix potential problems with conflicting modules
      var slots = ListBuffer[Map[Int, RequiredSession]]()
      t.toSeq.sortBy(-_._1.durationInHours).foreach(e =>{
        val options = slots.filter(l => e._2.forall(i => !l.contains(i)))
        var choice : Map[Int, RequiredSession] = Map()
        if (!options.isEmpty){
          choice = options.maxBy(_.size)
          slots -= choice
        }
        e._2.foreach(i => choice += (i -> e._1))
        slots += choice
      })

      // get schedule for term
      val termSchedule = binPackScheduleI(daysToGenerate, rooms,
        slots.map(s => new Event(s.map(_._2.durationInHours).max, s)).toSet,
        filters , weights)

      val fmap = modules.map(m => m.sessionStructure.map(_.session -> m)).flatten.toMap
      // unpack schedule
      if (termSchedule.isDefined){
        termSchedule.get.foreach(s => s.events.foreach(eo => eo._2.events.foreach( e=>
            schedule += new ScheduledSession(s.period.calendar.getDayOfWeek.toString, e._1, s.room, eo._1, e._2, fmap(e._2))
          )))

      }else{
        return None
      }

    })

    Some(schedule.map(s => {
      val daysToAdd = (7* s.week) + (s.day match {
        case "MONDAY" => 0
        case "TUESDAY"=> 1
        case "WEDNESDAY"=> 2
        case "THURSDAY"=> 3
        case "FRIDAY"=> 4
      })

      new ScheduledClass(new Period(startDay.calendar.plusDays(daysToAdd), startDay.timePeriod), s.time, s.room, s.session, s.module)
    }).toList)
  }


  def binPackScheduleI(daysToGenerate: Int, rooms: ArrayBuffer[Room], events: Set[Event],
                      filters: Seq[(Seq[RoomSchedule], Seq[Event], RoomSchedule) => Seq[Event]],
                      weights: Option[Seq[(Seq[RoomSchedule], Event, RoomSchedule) => Double]] =
                      None
                     ): Option[List[RoomSchedule]] = {
    val periods = for (i <- 1 until daysToGenerate + 1) yield getPeriodDefault(i)

    val schedule = rooms.flatMap(r => periods.map(new RoomSchedule(r, _)))

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
        // Apply the filters to the unscheduled events, to find those that can be scheduled in the slot
        var validEvents = filters.foldLeft(unProcEvents) { (r, f) => f(schedule, r, mostFree).to[ListBuffer] }

        if (validEvents.isEmpty) {
          // no valid events can go in that slot
          /*TODO:
              Consider an approach that will assign free time, allowing the room to be scheduled later
              This will help prevent possible issues, where an early slot in a room is unscheduleable,
              which blocks the room being used later
           */
          scheduleMap += (mostFree -> false)
        } else {
          // Apply the weighting functions
          if (weights.isDefined)
            validEvents = validEvents.groupBy(s => weights.get.map(_ (schedule, s, mostFree)).sum).maxBy(_._1)._2
          // Get the longest events
          validEvents = validEvents.groupBy(_.duration).maxBy(_._1)._2

          // Schedule the event
          mostFree + validEvents(0)
          unProcEvents -= validEvents(0)
        }

      }
    }

    Some(schedule.toList)
  }

  def willFit(currentSchedule: Seq[RoomSchedule], events: Seq[Event], room: RoomSchedule): Seq[Event] =
    events.filter(room.timeRemaining >= _.duration)


  class RoomSchedule(val room: Room, val period: Period) {
    var timeRemaining = (period.timePeriod.duration() / 60 / 60 / 1000).toFloat
    var durationPointer = period.timePeriod.start
    var events = mutable.HashMap[TimePeriod, Event]()

    def +(event: Event): Unit = {
      events += new TimePeriod(durationPointer, durationPointer.plus(event.duration.toLong, ChronoUnit.HOURS)) -> event
      timeRemaining -= event.duration
      durationPointer = durationPointer.plus(event.duration.toLong, ChronoUnit.HOURS)
    }

  }

  class Event (val duration: Float, val events:Map[Int, RequiredSession])

  class ScheduledSession(val day: String, val week: Int, val room:Room, val time: TimePeriod, val session:RequiredSession, val module: ModuleFehqLevel)
}
