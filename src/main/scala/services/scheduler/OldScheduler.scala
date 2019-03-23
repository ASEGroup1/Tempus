//package services.scheduler
//
//import java.time.temporal.{ChronoField, ChronoUnit}
//import java.time.{OffsetDateTime, OffsetTime, ZoneOffset}
//
//import entities.timing.TimePeriod
//import services.generator.eventgenerator.{Event, EventGenerator}
//import services.scheduler.poso.{Period, ScheduledClass}
//import services.sussexroomscraper.SussexRoomScraper
//
//import scala.collection.mutable
//
//object OldScheduler {
//
//  def generateSchedule(eventCount: Int, roomCount: Int): Option[Seq[ScheduledClass]] = {
//    OldScheduler.schedule(
//      SussexRoomScraper.roomDataForSession,
//      EventGenerator.generate(eventCount),
//      Array(
//        new Period(OffsetDateTime.parse("2019-01-01"), new TimePeriod {
//          start = OffsetTime.of(8, 0, 0, 0, ZoneOffset.UTC)
//          end = OffsetTime.of(20, 0, 0, 0, ZoneOffset.UTC)
//        }),
//        new Period(OffsetDateTime.parse("2019-01-02"), new TimePeriod {
//          start = OffsetTime.of(8, 0, 0, 0, ZoneOffset.UTC)
//          end = OffsetTime.of(20, 0, 0, 0, ZoneOffset.UTC)
//        }),
//        new Period(OffsetDateTime.parse("2019-01-03"), new TimePeriod {
//          start = OffsetTime.of(8, 0, 0, 0, ZoneOffset.UTC)
//          end = OffsetTime.of(20, 0, 0, 0, ZoneOffset.UTC)
//        }),
//        new Period(OffsetDateTime.parse("2019-01-04"), new TimePeriod {
//          start = OffsetTime.of(8, 0, 0, 0, ZoneOffset.UTC)
//          end = OffsetTime.of(20, 0, 0, 0, ZoneOffset.UTC)
//        }),
//        new Period(OffsetDateTime.parse("2019-01-05"), new TimePeriod {
//          start = OffsetTime.of(8, 0, 0, 0, ZoneOffset.UTC)
//          end = OffsetTime.of(20, 0, 0, 0, ZoneOffset.UTC)
//        }),
//      ))
//  }
//
//  // best fit bin packing
//  def schedule(rooms: Seq[Room], events: Seq[Event], periods: Seq[Period]): Option[Seq[ScheduledClass]] =
//    scheduleI(rooms.flatMap(r => periods.map(p => r -> p)), events)
//
//  /**
//    * @param areas  a Set of tuples of all permutations of rooms and periods
//    * @param events classes, etc.
//    */
//  def scheduleI(areas: Seq[(Room, Period)], events: Seq[Event]): Option[Seq[ScheduledClass]] = {
//    // ordered best fit bin packing, packing "events" into "room" number of bins of size "period"
//    val schedule = areas.map(a => new RoomSchedule(a._1, a._2))
//
//    // for each event (largest first), find the smallest slot (room and time) that it fits in, then add it to that slot.
//    events.sortBy(_.duration.get(ChronoField.MILLI_OF_DAY))(Ordering[Int].reverse).foreach(e => {
//      val availableRooms = {
//        schedule.filter(_.timeRemaining >= e.duration.get(ChronoField.MILLI_OF_DAY))
//      }
//
//      if (availableRooms.isEmpty) return None
//      else availableRooms.min(Ordering by[RoomSchedule, Int] (_.timeRemaining)) + e
//    })
//
//    Some(schedule.flatMap(_()))
//  }
//
//  private class RoomSchedule(val room: entities.locations.Room, val period: Period) {
//    var timeRemaining = period.timePeriod.duration()
//    var durationPointer = period.timePeriod.start
//    var events = mutable.HashMap[TimePeriod, Event]()
//
//    def +(event: Event): Unit = {
//      var duration = event.duration.get(ChronoField.MILLI_OF_DAY)
//      events += (new TimePeriod {
//        start = durationPointer
//        end = durationPointer.plus(duration, ChronoUnit.MILLIS)
//      } -> event)
//      timeRemaining -= duration
//      durationPointer = durationPointer.plus(duration, ChronoUnit.MILLIS)
//    }
//
//    def apply() = events.map(e => new ScheduledClass(period, e._1, room, e._2)).toList
//  }
//
//}
