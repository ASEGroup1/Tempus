import junit.framework.TestCase
import org.junit.Assert._
import services.scheduler.Scheduler

class SchedulerTests extends TestCase {

  def testIfScheduleIncludesAllEvents =
    assertEquals(1917, Scheduler.binPackSchedule(5).get.size)

  def testIfNoEventsGenerateEmptySchedule = assertEquals(None,  Scheduler.binPackSchedule(0))

  def testIfEventsDoNotIntersect = {
    var currentEnd = -1
    var currentDay = -1
    var currentRoom = ""

    Scheduler.binPackSchedule(5).get.groupBy(sc => (sc.room.roomName, sc.day.calendar.getDayOfMonth)).foreach(s => {
		  s._2.sortBy(sc => (sc.time.start.getHour, sc.time.start.getMinute)).foreach(e => {
			  print(s"[Day: ${e.day.calendar.getDayOfMonth} Room: ${e.room.roomName} Start time: ${e.time.start.getHour} < ")

        //When room changes reset currentEnd
        if(currentRoom != e.room.roomName) {
          currentRoom = e.room.roomName
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
    assertEquals(None,  Scheduler.binPackSchedule(1))
  }
}
