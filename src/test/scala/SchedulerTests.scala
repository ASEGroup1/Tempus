import junit.framework.TestCase
import org.junit.Assert._
import services.parser.TimeTableParser
import services.scheduler.Scheduler
import services.sussexroomscraper.SussexRoomScraper

import scala.util.Random

class SchedulerTests extends TestCase {
  val rooms = SussexRoomScraper.roomDataForSession
  val events = getMods

  def getMods() ={
    val mods = TimeTableParser.modules.flatMap(m => m._2.requiredSessions.map(_ -> m._2)).toSet
    // Temporary until a weekly approach is implemented
    Random.shuffle(mods).drop(Math.round(mods.size/8))
  }

  def testIfScheduleIncludesAllEvents =
    assertEquals(1915, Scheduler.binPackSchedule(5, rooms, events).get.size)

  def testIfNoEventsGenerateEmptySchedule = assertEquals(None, Scheduler.binPackSchedule(0, rooms, events))

  def testIfEventsDoNotIntersect = {
    var currentEnd = -1
    var currentDay = -1
    var currentRoom = ""

    Scheduler.binPackSchedule(5, rooms, events).get.groupBy(sc => (sc.room.roomName, sc.day.calendar.getDayOfMonth)).foreach(s => {
      s._2.sortBy(sc => (sc.time.start.getHour, sc.time.start.getMinute)).foreach(e => {
        print(s"[Day: ${e.day.calendar.getDayOfMonth} Room: ${e.room.roomName} Start time: ${e.time.start.getHour} End time: ${e.time.end.getHour} module: ${e.className}] < ")

        //When room changes reset currentEnd
        if (currentRoom != e.room.roomName) {
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
    assertEquals(None, Scheduler.binPackSchedule(1, rooms, events))
  }
}
