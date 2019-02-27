import junit.framework.TestCase
import org.joda.time.{DateTime, LocalTime}
import org.junit.Assert._
import services.generator.eventgenerator.EventGenerator
import services.generator.roomgenerator.RoomGenerator
import services.scheduler.Scheduler
import services.scheduler.poso.{Duration, Period, Room, ScheduledClass}

class SchedulerTests extends TestCase {

  def testIfScheduleIncludesAllEvents = assertEquals(100, Scheduler.generateSchedule(100, 10).get.size)

  def testIfNoEventsGenerateEmptySchedule = assertEquals(Vector(), Scheduler.generateSchedule(0, 0).get)

  def testIfEventsDoNotIntersect = {
    var currentEnd = -1
    var currentDay = -1
    var currentRoom = ""

    Scheduler.generateSchedule(100, 10).get.groupBy(sc => (sc.room.name, sc.day.calendar.dayOfMonth())).foreach(s => {
      s._2.sortBy(sc =>  (sc.time.start.getHourOfDay, sc.time.start.getMinuteOfHour)).foreach(e => {
        print("[Day: " + e.day.calendar.getDayOfMonth + ", Room: " + e.room.name + ", Start time: " + e.time.start.getHourOfDay + "] < ")

        //When room changes reset currentEnd
        if(currentRoom != e.room.name) {
          currentRoom = e.room.name
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
    assertEquals(None, Scheduler.generateSchedule(100, 1))
  }
}