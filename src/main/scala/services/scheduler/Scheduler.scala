package services.scheduler

import java.time.temporal.{ChronoField, ChronoUnit}
import java.time.{OffsetDateTime, OffsetTime, ZoneOffset}

import entities.timing.TimePeriod
import services.generator.eventgenerator.{Event, EventGenerator}
<<<<<<< HEAD
import services.scheduler.poso.{Duration, Period, Room, ScheduledClass}
import services.sussexroomscraper.SussexRoomScraper
=======
import services.generator.roomgenerator.RoomGenerator
import services.scheduler.poso.{Period, Room, ScheduledClass}
>>>>>>> T9-AddScalaOrmObjects

import scala.collection.mutable

object Scheduler {

<<<<<<< HEAD
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
=======
	def main(args: Array[String]): Unit = {
		val rooms = RoomGenerator.generate(10).map(new Room(_))
		val events = EventGenerator.generate(100)
		val periods = Array(
			new Period(
				OffsetDateTime.parse("01-01-19"),
				new TimePeriod() {
					start = OffsetTime.of(8,0,0,0,ZoneOffset.UTC)
					end = OffsetTime.of(20,0,0,0, ZoneOffset.UTC)
				}),
			new Period(
				OffsetDateTime.parse("02-01-19"),
				new TimePeriod() {
					start = OffsetTime.of(8,0,0,0,ZoneOffset.UTC)
					end = OffsetTime.of(20,0,0,0, ZoneOffset.UTC)
				}))

		val schedule = Scheduler.schedule(rooms, events, periods)
		if (schedule.isDefined) {
			println("\tRoom  Module       Start     End")
			schedule.get.groupBy(_.day.calendar.getDayOfWeek()).foreach(e => println(e._1.toString + "\n\t" + e._2.sortBy(s => (s.room.id, s.time.start.get(ChronoField.MILLI_OF_DAY))).mkString("\n\t") + "\n"))
		} else println("Could not generate a timetable")
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

		Some(schedule.flatMap(_ ()))
	}

	private class RoomSchedule(val room: Room, val period: Period) {
		var timeRemaining = period.timePeriod.duration()
		var startTime = period.timePeriod.start
		var events = mutable.HashMap[TimePeriod, Event]()

		def +(event: Event): Unit = {
			var duration = event.duration.getMillisOfDay
			events += new TimePeriod {
				start = startTime
				end = startTime.plus(duration, ChronoUnit.MILLIS)
			} -> event
			timeRemaining -= duration
			startTime = startTime.plus(duration, ChronoUnit.MILLIS)

		}

		def apply() = events.map(e => new ScheduledClass(period, e._1, room, e._2)).toList
	}

}
>>>>>>> T9-AddScalaOrmObjects
