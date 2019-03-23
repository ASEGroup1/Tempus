import junit.framework.TestCase
import org.junit.Assert._
import services.scheduler.{OldScheduler, Scheduler}

class SchedulerTests extends TestCase {

  def testIfScheduleIncludesAllEvents = assertEquals(100, OldScheduler.generateSchedule(100, 10).get.size)

  def testIfNoEventsGenerateEmptySchedule = assertEquals(Vector(), OldScheduler.generateSchedule(0, 0).get)

  def testIfEventsDoNotIntersect = {
    var currentEnd = -1
    var currentDay = -1
    var currentRoom = ""

    Scheduler.generateSchedule(100, 10).get.groupBy(sc => (sc.room.name, sc.day.calendar.getDayOfMonth())).foreach(s => {
		  s._2.sortBy(sc => (sc.time.start.getHour, sc.time.start.getMinute)).foreach(e => {
			  print("[Day: " + e.day.calendar.getDayOfMonth + ", Room: " + e.room.name + ", Start time: " + e.time.start.getHour + "] < ")

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

			  if (currentEnd > e.time.start.getHour) fail(currentEnd + " > " + e.time.start.getHour)
			  currentEnd = e.time.end.getHour
      })
    })
  }

  def testIfEventsCannotFitInSchedule: Unit = {
    assertEquals(None, OldScheduler.generateSchedule(10000, 1))
  }
}
