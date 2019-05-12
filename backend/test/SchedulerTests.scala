import junit.framework.TestCase
import org.junit.Assert._
import services.parser.TimeTableParser
import services.scheduler.Scheduler
import services.sussexroomscraper.SussexRoomScraper

class SchedulerTests extends TestCase {
  val rooms = SussexRoomScraper.roomDataForSession
  val events = TimeTableParser.modules

  def testIfScheduleHasValidDisabledAccessAndRoomCapacity =  assertTrue(Scheduler.binPackSchedule(5, rooms, events).get.forall(s => {
    val module = TimeTableParser.moduleMap(s.className)
    (s.room.roomCapacity >= module.studentCount) && (
      if(module.disabledAccess){
        s.room.disabledAccess
      }else{
        true
      })
  }))

  def testIfScheduleIncludesAllEvents =
    assertEquals(79302, Scheduler.binPackSchedule(5, rooms, events).get.size)

  def testIfNoEventsGenerateEmptySchedule = assertEquals(None, Scheduler.binPackSchedule(0, rooms, events))

  def testIfEventsDoNotIntersect = {
    var currentEnd = -1
    var currentDay = -1
    var currentRoom = ""

    Scheduler.binPackSchedule(5, rooms, events).get.groupBy(sc => (sc.room.roomName, sc.day.calendar.getDayOfYear)).foreach(s => {
      s._2.sortBy(sc => (sc.time.start.getHour, sc.time.start.getMinute)).foreach(e => {
        print(s"[Day: ${e.day.calendar.getDayOfYear} Room: ${e.room.roomName} Start time: ${e.time.start.getHour} End time: ${e.time.end.getHour} module: ${e.className}] < ")

        //When room changes reset currentEnd
        if (currentRoom != e.room.roomName) {
          currentRoom = e.room.roomName
          currentEnd = -1
        }

        //When day changes reset currentEnd
        if (currentDay != e.day.calendar.getDayOfYear) {
          currentDay = e.day.calendar.getDayOfYear
          currentEnd = -1
        }

        if (currentEnd > e.time.start.getHour) fail(currentEnd + " > " + e.time.start.getHour)
        currentEnd = e.time.end.getHour
      })
    })
  }

  def testIfEventsCannotFitInSchedule: Unit = {
    assertEquals(None, Scheduler.binPackSchedule(1, rooms, events))
  }

}
