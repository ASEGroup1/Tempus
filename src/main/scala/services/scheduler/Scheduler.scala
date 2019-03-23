package services.scheduler

import java.time.temporal.ChronoUnit
import java.time.{OffsetDateTime, OffsetTime, ZoneOffset}

import entities.locations.Room
import entities.timing.TimePeriod
import services.parser.TimeTableParser
import services.scheduler.poso.{Period, ScheduledClass}
import services.sussexroomscraper.SussexRoomScraper

import scala.collection.mutable

object Scheduler {
  val rooms = SussexRoomScraper.roomDataForSession
  val events = TimeTableParser.modules.flatMap(m => m._2.requiredSessions.map(m._1 -> _.durationInHours)).toSet
  val scheduleDays = Array(getPeriodDefault(1), getPeriodDefault(2), getPeriodDefault(3), getPeriodDefault(4), getPeriodDefault(5))

  def getPeriodDefault(dayOfMonth: Int) = getPeriod(dayOfMonth, 1, 8, 20)

  def getPeriod(dayOfMonth: Int, monthOfYear:Int, beginHour24: Int, endHour24: Int) =
    new Period(OffsetDateTime.parse(s"2019-$dayOfMonth-$monthOfYear"), new TimePeriod {
    start = OffsetTime.of(beginHour24, 0, 0, 0, ZoneOffset.UTC)
    end = OffsetTime.of(endHour24, 0, 0, 0, ZoneOffset.UTC)
  })

  def binPackSchedule(daysToGenerate:Int): Option[List[ScheduledClass]] = {
    val rooms = SussexRoomScraper.roomDataForSession
    val events = TimeTableParser.modules.flatMap(m => m._2.requiredSessions.map(m._1 -> _.durationInHours)).toSet
    val periods = for (i <- 0 until daysToGenerate) yield getPeriodDefault(i)


    val schedule = rooms.flatMap(r => periods.map(new RoomSchedule(r, _)))
    events.foreach(e => if (!schedule.exists(_.timeRemaining >= e._2)) return None)
    Some(schedule.flatMap(_()).toList)
  }

  private class RoomSchedule(val room: Room, val period: Period) {
    var timeRemaining = period.timePeriod.duration()
    var durationPointer = period.timePeriod.start
    var events = mutable.HashMap[TimePeriod, String]()

    def +(event: (String, Float)): Unit = {
      events += new TimePeriod(durationPointer, durationPointer.plus(event._2.toLong, ChronoUnit.MILLIS)) -> event._1
      timeRemaining -= event._2
      durationPointer = durationPointer.plus(event._2.toLong, ChronoUnit.MILLIS)
    }

    def apply() = events.map(e => new ScheduledClass(period, e._1, room, e._2)).toList
  }
}
