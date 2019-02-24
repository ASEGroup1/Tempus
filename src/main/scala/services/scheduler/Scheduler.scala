package services.scheduler

import org.joda.time.{DateTime, LocalTime}
import services.generator.eventgenerator.{Event, EventGenerator}
import services.generator.roomgenerator.RoomGenerator
import services.scheduler.poso.{Duration, Period, Room, ScheduledClass}

import scala.collection.mutable

object Scheduler {

  // best fit bin packing
  def schedule(rooms: Seq[Room], events: Seq[Event], periods: Seq[Period], filters: Seq[(Seq[RoomSchedule], Event, RoomSchedule) => Boolean]): Option[Seq[ScheduledClass]] =
    scheduleI(rooms.flatMap(r => periods.map(p => r -> p)), events, filters)

  /**
    * @param areas  a Set of tuples of all permutations of rooms and periods
    * @param events classes, etc.
    */
  def scheduleI(areas: Seq[(Room, Period)], events: Seq[Event], filters: Seq[(Seq[RoomSchedule], Event, RoomSchedule) => Boolean]): Option[Seq[ScheduledClass]] = {
    if (!isSchedulePlausible(areas, events)) {
      None
    } else {
      // ordered best fit bin packing, packing "events" into "room" number of bins of size "period"
      val schedule = areas.map(a => new RoomSchedule(a._1, a._2))

      // for each event (largest first), find the smallest slot (room and time) that it fits in, then add it to that slot.
      events.sortBy(_.duration.getMillisOfDay)(Ordering[Int].reverse).foreach(e => {
        val availableRooms = schedule.filter(s => filters.forall(_ (schedule, e, s)))

        if (availableRooms.isEmpty) return None
        else availableRooms.min(Ordering by[RoomSchedule, Int] (_.timeRemaining)) + e
      })

      Some(schedule.flatMap(_ ()))
    }
  }

  def willFit(schedule: Seq[RoomSchedule], event: Event, room: RoomSchedule): Boolean =
    room.timeRemaining >= event.duration.getMillisOfDay

  def noIntersect(schedule: Seq[RoomSchedule], event: Event, room: RoomSchedule): Boolean = {
    val duration = new Duration(room.durationPointer, room.durationPointer.plusMillis(event.duration.getMillisOfDay))

    // same day and time range
    val eventsSameTime = schedule.filter(s => s.period.calendar == room.period.calendar &&
      s.period.duration.canContain(duration)
    )

    // events that occur at the same time
    val events = eventsSameTime.flatMap(e => e.events.filter(p => p._1.overlap(duration)))

    // check no events running share the module
    events.forall(_._2.name != event.name)
  }

  /**
    * Performs checks against various criteria, to rule out inputs without a valid solution
    *
    * @param areas
    * @param events
    * @return
    */
  private def isSchedulePlausible(areas: Seq[(Room, Period)], events: Seq[Event]): Boolean =
    areas.map(_._2.duration.durationMins).sum > events.map(e => ((e.duration.getHourOfDay * 60) + e.duration.getMinuteOfHour)).sum


  class RoomSchedule(val room: Room, val period: Period) {
    var timeRemaining = period.duration.duration()
    var durationPointer = new LocalTime(period.duration.start)
    var events = mutable.HashMap[Duration, Event]()

    def +(event: Event): Unit = {
      var duration = event.duration.getMillisOfDay
      events += (new Duration(new LocalTime(durationPointer), new LocalTime(durationPointer.plusMillis(duration))) -> event)
      timeRemaining -= duration
      durationPointer = durationPointer.plusMillis(duration)
    }

    def apply() = events.map(e => new ScheduledClass(period, e._1, room, e._2)).toList
  }

  def main(args: Array[String]): Unit = {
    val rooms = RoomGenerator.generate(10).map(new Room(_))
    val events = EventGenerator.generate(10)
    val periods = Array(
      new Period(DateTime.parse("01-01-19"), new Duration(new LocalTime(8, 0), new LocalTime(20, 0))),
      new Period(DateTime.parse("02-01-19"), new Duration(new LocalTime(8, 0), new LocalTime(20, 0)))
    )

    val schedule = Scheduler.schedule(rooms, events, periods, Array(
      willFit(_, _, _), noIntersect(_, _, _)
    ))
    if (schedule.isDefined) {
      println("\tRoom  Module       Start     End")
      schedule.get.groupBy(_.day.calendar.dayOfWeek()).foreach(e => println(e._1.getAsShortText + "\n\t" + e._2.toSeq.sortBy(s => (s.room.id, s.time.start.getMillisOfDay)).mkString("\n\t") + "\n"))
    } else println("Could not generate a timetable")
  }
}