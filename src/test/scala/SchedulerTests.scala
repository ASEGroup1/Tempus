import java.time.{OffsetDateTime, OffsetTime, ZoneOffset}

import entities.timing.TimePeriod
import junit.framework.TestCase
import org.junit.Assert._
import services.generator.eventgenerator.EventGenerator
import services.generator.roomgenerator.RoomGenerator
import services.scheduler.Scheduler
import services.scheduler.poso.{Period, Room, ScheduledClass}

class SchedulerTests extends TestCase {
	def generateSchedule(eventCount: Int, roomCount: Int): Option[Seq[ScheduledClass]] = {
		Scheduler.schedule(
			RoomGenerator.generate(roomCount).map(new Room(_)),
			EventGenerator.generate(eventCount),
			Array(
				new Period(OffsetDateTime.parse("2019-01-01"), new TimePeriod {
					start = OffsetTime.of(8, 0, 0, 0, ZoneOffset.UTC)
					end = OffsetTime.of(20, 0, 0, 0, ZoneOffset.UTC)
				}),
				new Period(OffsetDateTime.parse("2019-01-02"), new TimePeriod {
					start = OffsetTime.of(8, 0, 0, 0, ZoneOffset.UTC)
					end = OffsetTime.of(20, 0, 0, 0, ZoneOffset.UTC)
				})
			))
	}

	def testIfScheduleIncludesAllEvents = assertEquals(100, generateSchedule(100, 10).get.size)

	def testIfNoEventsGenerateEmptySchedule = assertEquals(Vector(), generateSchedule(0, 0).get)

	def testIfEventsDoNotIntersect = {
		var currentEnd = -1
		var currentDay = -1
		var currentRoom = - 1

		generateSchedule(100, 10).get.groupBy(sc => (sc.room.id, sc.day.calendar.getDayOfMonth())).foreach(s => {
			s._2.sortBy(sc =>  (sc.time.start.getHour, sc.time.start.getMinute)).foreach(e => {
				print("[Day: " + e.day.calendar.getDayOfMonth + ", Room: " + e.room.id + ", Start time: " + e.time.start.getHour + "] < ")

				//When room changes reset currentEnd
				if(currentRoom != e.room.id) {
					currentRoom = e.room.id
					currentEnd = -1
				}

				//When day changes reset currentEnd
				if (currentDay != e.day.calendar.getDayOfMonth) {
					currentDay = e.day.calendar.getDayOfMonth
					currentEnd = -1
				}

				if (currentEnd > e.time.start.getHour) fail(currentEnd + " > " + e.time.start.getHour)
				currentEnd = e.time.end.getHour
			})
		})
	}

	def testIfEventsCannotFitInSchedule: Unit = {
		assertEquals(None, generateSchedule(100, 1))
	}
}
