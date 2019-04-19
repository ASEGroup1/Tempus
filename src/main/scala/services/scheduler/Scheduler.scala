package services.scheduler

import java.time.temporal.ChronoUnit
import java.time.{OffsetDateTime, OffsetTime, ZoneOffset}

import entities.locations.Room
import entities.module.{Module, RequiredSession}
import entities.timing.TimePeriod
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

  def binPackSchedule(daysToGenerate: Int, rooms: ArrayBuffer[Room], modules: Set[Module]): Option[List[ScheduledClass]] = {
    binPackSchedule(daysToGenerate, rooms, modules, Array(willFit(_, _)))
  }

  def binPackSchedule(daysToGenerate: Int, rooms: ArrayBuffer[Room], modules: Set[Module],
                      filters: Seq[(Seq[ScheduleInterfaceMapper], Seq[ScheduleInterfaceMapper]) => Seq[ScheduleInterfaceMapper]],
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
        filters, weights)

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
                       filters: Seq[(Seq[ScheduleInterfaceMapper], Seq[ScheduleInterfaceMapper]) => Seq[ScheduleInterfaceMapper]],
                       weights: Option[Seq[(Seq[ScheduleInterfaceMapper], ScheduleInterfaceMapper) => Double]] =
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

        // put scheduled and unscheduled events into an interface that maps thier values
        val wrapped = wrap(schedule, unProcEvents, mostFree)
        // Apply the filters to the unscheduled events, to find those that can be scheduled in the slot
        var validEventsWrapped = filters.foldLeft(wrapped._2) { (r, f) => f(wrapped._1, r).to[ListBuffer] }

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
            validEventsWrapped = validEventsWrapped.groupBy(s => weights.get.map(_ (wrapped._1, s)).sum).maxBy(_._1)._2
          // Get the longest events
          validEventsWrapped = validEventsWrapped.groupBy(_.event.duration).maxBy(_._1)._2

          // Schedule the event
          mostFree + validEventsWrapped(0).event
          unProcEvents -= validEventsWrapped(0).event
        }

      }
    }

    Some(schedule.toList)
  }

  def willFit(filledSlots: Seq[ScheduleInterfaceMapper], possibleSlots: Seq[ScheduleInterfaceMapper]): Seq[ScheduleInterfaceMapper] =
    possibleSlots.filter(s => s.roomSchedule.timeRemaining >= s.event.duration)


  /**
    * Function to process constraints where both the where and body functions are commutative.
    * i.e. where(a,b) = where(b,a) and body(a,b) = body(b,a).
    *
    * This function will remove all possibleSlots where there is an (a,b) where one is an instance of  filledSlots and the other of possibleSlots:
    * where(a,b) is true, and body(a,b) is false.
    *
    * @param filledSlots   currently scheduled events
    * @param possibleSlots events that could be scheduled
    * @param where
    * @param body
    * @return
    */
  def RRWrap(filledSlots: Seq[ScheduleInterfaceMapper], possibleSlots: Seq[ScheduleInterfaceMapper], where: (ScheduleInterfaceMapper, ScheduleInterfaceMapper) => Boolean, body: (ScheduleInterfaceMapper, ScheduleInterfaceMapper) => Boolean): Seq[ScheduleInterfaceMapper] =
  // get applicable entries using the where
    possibleSlots.filterNot(possibleSlots.map(a => (a, filledSlots.filter(b =>
      where(a, b)
      // get a list of all applicable entries that dont pass the constraint
    ))).filterNot(g => {
      val a = g._1
      g._2.forall(b =>
        body(a, b)
      )
      // remove them and return
    }).toSet)

  /**
    * Function to process constraints where the where function is commutative.
    * i.e. where(a,b) = where(b,a) and body(a,b) = body(b,a).
    *
    * This function will remove all possibleSlots where there is an (a,b) where one is an instance of  filledSlots and the other of possibleSlots:
    * where(a,b) is true, and body(a,b) is false.
    *
    * @param filledSlots   currently scheduled events
    * @param possibleSlots events that could be scheduled
    * @param where
    * @param body
    * @return
    */
  def RNWrap(filledSlots: Seq[ScheduleInterfaceMapper], possibleSlots: Seq[ScheduleInterfaceMapper], where: (ScheduleInterfaceMapper, ScheduleInterfaceMapper) => Boolean, body: (ScheduleInterfaceMapper, ScheduleInterfaceMapper) => Boolean): Seq[ScheduleInterfaceMapper] =
  // get applicable entries using the where
    possibleSlots.filterNot(possibleSlots.map(a => (a, filledSlots.filter(b =>
      where(a, b)
      // get a list of all applicable entries that dont pass the constraint
      //
    ))).filterNot(g => {
      val a = g._1
      g._2.forall(b =>
        body(a, b) || body(b, a)
      )
    }).toSet)


  /**
    * Function to process constraints where the where function is noncommutative.
    * i.e. if where(a,b) is true does not mean where(b,a) is true
    *
    * This function will remove all possibleSlots where there is an (a,b) where one is an instance of  filledSlots and the other of possibleSlots:
    * where(a,b) is true, and body(a,b) is false.
    *
    * @param filledSlots   currently scheduled events
    * @param possibleSlots events that could be scheduled
    * @param where
    * @param body
    * @return
    */
  def NRNNWrap(filledSlots: Seq[ScheduleInterfaceMapper], possibleSlots: Seq[ScheduleInterfaceMapper], where: (ScheduleInterfaceMapper, ScheduleInterfaceMapper) => Boolean, body: (ScheduleInterfaceMapper, ScheduleInterfaceMapper) => Boolean): Seq[ScheduleInterfaceMapper] =
    possibleSlots.filterNot((possibleSlots.map(a => (a, filledSlots.filter(b =>
      where(a, b)
    ))).filterNot(g => {
      val a = g._1
      g._2.forall(b =>
        body(a, b)
      )
    }) ++ possibleSlots.map(a => (a, filledSlots.filter(b =>
      where(b, a)
    ))).filterNot(g => {
      val a = g._1
      g._2.forall(b =>
        body(b, a)
      )
    })).toSet)


  def sWrap(filledSlots: Seq[ScheduleInterfaceMapper], possibleSlots: Seq[ScheduleInterfaceMapper], where: (ScheduleInterfaceMapper) => Boolean, body: (ScheduleInterfaceMapper) => Boolean): Seq[ScheduleInterfaceMapper] =
    possibleSlots.filterNot(possibleSlots.filter(where(_)).filterNot(body(_)).toSet)

  /**
    * A method that will allow functions to use indivdual module information.
    * This function exsits, as there is an overhead with adding the information.
    * @param a first parameter
    * @param b second parameter
    * @param func function to apply
    * @return True, if func(a,b) is true, for every pair of events that run in the same week
    */
  def mapEvents(a: ScheduleInterfaceMapper, b: ScheduleInterfaceMapper, func: (ScheduleInterfaceMapper, ScheduleInterfaceMapper) => Boolean): Boolean = (
    // Get each pair of sessions that happen in the same week
    for {al <- a.event.events; bl <- b.event.events; if al._1 == bl._1}
      // add the sessions to the interfaceMapper
      yield (a.withRequiredSession(al), b.withRequiredSession(bl)))
    // Check the constraint holds for all pairs
    .forall(p => func(p._1, p._2))


  def wrap(currentSchedule: Seq[RoomSchedule], events: Seq[Event], room: RoomSchedule): (Seq[ScheduleInterfaceMapper], Seq[ScheduleInterfaceMapper]) =
    (
      currentSchedule.flatMap(s => s.events.map(t => new ScheduleInterfaceMapper(s, t._1))),
      events.map(new ScheduleInterfaceMapper(room, _))
    )

}




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

class Event(val duration: Float, val events: Map[Int, RequiredSession])
