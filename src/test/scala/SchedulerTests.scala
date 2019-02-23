import junit.framework.TestCase
import org.joda.time.{DateTime, LocalTime}
import org.junit.Assert._
import services.generator.eventgenerator.EventGenerator
import services.generator.roomgenerator.RoomGenerator
import services.scheduler.Scheduler
import services.scheduler.poso.{Duration, Period, Room, ScheduledClass}

class SchedulerTests extends TestCase {
  def generateSchedule(eventCount: Int, roomCount: Int): Option[Seq[ScheduledClass]] = {
    Scheduler.schedule(
      RoomGenerator.generate(roomCount).map(new Room(_)),
      EventGenerator.generate(eventCount),
      Array(
        new Period(DateTime.parse("2019-01-01"), new Duration(new LocalTime(8, 0), new LocalTime(20, 0))),
        new Period(DateTime.parse("2019-01-02"), new Duration(new LocalTime(8, 0), new LocalTime(20, 0)))
      ))
  }

  def testIfScheduleIncludesAllEvents = assertEquals(100, generateSchedule(100, 10).get.size)

  def testIfNoEventsGenerateEmptySchedule = assertEquals(Vector(), generateSchedule(0, 0).get)

  def testIfEventsDoNotIntersect = {
    var currentEnd = -1
    var currentDay = -1
    var currentRoom = - 1

    generateSchedule(100, 10).get.groupBy(sc => (sc.room.id, sc.day.calendar.dayOfMonth())).foreach(s => {
      s._2.sortBy(_.time.start.getHourOfDay).foreach(e => {
        print("[Day:" + currentDay + ", Room: " + e.room.id + ", end time:" + currentEnd + "] < " +
          "[Day: " + e.day.calendar.getDayOfMonth + ", Room: " + e.room.id + ", Start time: " + e.time.start.getHourOfDay + "] < ")

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

        if (currentEnd > e.time.start.getHourOfDay) fail(currentEnd + " > " + e.time.start.getHourOfDay)
        currentEnd = e.time.end.getHourOfDay
      })
    })
  }

  def testIfEventsCannotFitInSchedule: Unit = {
    assertEquals(None, generateSchedule(100, 1))
  }
}