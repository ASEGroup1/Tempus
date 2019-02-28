package services.scheduler

import org.joda.time.{DateTime, LocalTime}
import services.generator.eventgenerator.{Event, EventGenerator}
import services.scheduler.poso.{Duration, Period, Room, ScheduledClass}
import services.sussexroomscraper.SussexRoomScraper

import scala.collection.mutable

object Scheduler {

  def generateSchedule(eventCount: Int, roomCount: Int): Option[Seq[ScheduledClass]] = {
    Scheduler.schedule(
      SussexRoomScraper.roomDataForSession,
      EventGenerator.generate(eventCount),
      Array(
        new Period(DateTime.parse("2019-01-01"), Duration(8, 20)),
        new Period(DateTime.parse("2019-01-02"), Duration(8, 20)),
        new Period(DateTime.parse("2019-01-03"), Duration(8, 20)),
        new Period(DateTime.parse("2019-01-04"), Duration(8, 20)),
        new Period(DateTime.parse("2019-01-05"), Duration(8, 20)),
      ))
  }

  // best fit bin packing
  def schedule(rooms: Seq[Room], events: Seq[Event], periods: Seq[Period]): Option[Seq[ScheduledClass]] =
    scheduleI(rooms.flatMap(r => periods.map(p => r -> p)), events)

  /**
    * @param areas  a Set of tuples of all permutations of rooms and periods
    * @param events classes, etc.
    */
  def scheduleI(areas: Seq[(Room, Period)], events: Seq[Event]): Option[Seq[ScheduledClass]] = {
    // ordered best fit bin packing, packing "events" into "room" number of bins of size "period"
    val schedule = areas.map(a => new RoomSchedule(a._1, a._2))

    // for each event (largest first), find the smallest slot (room and time) that it fits in, then add it to that slot.
    events.sortBy(_.duration.getMillisOfDay)(Ordering[Int].reverse).foreach(e => {
      val availableRooms = schedule.filter(_.timeRemaining >= e.duration.getMillisOfDay)

      if (availableRooms.isEmpty) return None
      else availableRooms.min(Ordering by[RoomSchedule, Int] (_.timeRemaining)) + e
    })

    Some(schedule.flatMap(_()))
  }

  private class RoomSchedule(val room: Room, val period: Period) {
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
}