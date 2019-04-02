package services.scheduler

import java.time.temporal.ChronoUnit
import java.time.{OffsetDateTime, OffsetTime, ZoneOffset}

import entities.locations.Room
import entities.module.RequiredSession
import entities.timing.TimePeriod
import services.parser.TimeTableParser
import services.scheduler.poso.{Period, ScheduledClass}
import services.sussexroomscraper.SussexRoomScraper

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object Scheduler {
  def getPeriodDefault(dayOfMonth: Int) = getPeriod(dayOfMonth, 1, 8, 20)

  def getPeriod(dayOfMonth: Int, monthOfYear: Int, beginHour24: Int, endHour24: Int) =
    new Period(OffsetDateTime.parse(s"2019-0$monthOfYear-0${dayOfMonth}T00:00:00+00:00"), new TimePeriod {
      start = OffsetTime.of(beginHour24, 0, 0, 0, ZoneOffset.UTC)
      end = OffsetTime.of(endHour24, 0, 0, 0, ZoneOffset.UTC)
    })


  def binPackSchedule(daysToGenerate: Int, rooms: ArrayBuffer[Room], events: Set[(String, Float)]): Option[List[ScheduledClass]] = {
    val periods = for (i <- 1 until daysToGenerate + 1) yield getPeriodDefault(i)

    val schedule = rooms.flatMap(r => periods.map(new RoomSchedule(r, _)))

    events.foreach(e => {
      val availableRooms = schedule.filter(_.timeRemaining >= e._2)

      if (availableRooms.isEmpty) return None
      else availableRooms.min(Ordering by[RoomSchedule, Float] (_.timeRemaining)) + e
    })

    Some(schedule.flatMap(_ ()).toList)
  }

  private class RoomSchedule(val room: Room, val period: Period) {
    var timeRemaining = (period.timePeriod.duration() / 60 / 60 / 1000).toFloat
    var durationPointer = period.timePeriod.start
    var events = mutable.HashMap[TimePeriod, String]()

    def +(event: (String, Float)): Unit = {
      events += new TimePeriod(durationPointer, durationPointer.plus(event._2.toLong, ChronoUnit.HOURS)) -> event._1
      timeRemaining -= event._2
      durationPointer = durationPointer.plus(event._2.toLong, ChronoUnit.HOURS)
    }

    def apply() = events.map(e => new ScheduledClass(period, e._1, room, e._2)).toList
  }

}
