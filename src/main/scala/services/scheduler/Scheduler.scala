package services.scheduler

import java.time.temporal.ChronoUnit
import java.time.{OffsetDateTime, OffsetTime, ZoneOffset}

import entities.locations.Room
import entities.module.{Module, RequiredSession}
import entities.timing.TimePeriod
import services.scheduler.poso.{Period, ScheduledClass}

import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, ListBuffer}
import scala.util.Random

object Scheduler {
  def getPeriodDefault(dayOfMonth: Int) = getPeriod(dayOfMonth, 1, 8, 20)

  def getPeriod(dayOfMonth: Int, monthOfYear: Int, beginHour24: Int, endHour24: Int) =
    new Period(OffsetDateTime.parse(s"2019-0$monthOfYear-0${dayOfMonth}T00:00:00+00:00"), new TimePeriod {
      start = OffsetTime.of(beginHour24, 0, 0, 0, ZoneOffset.UTC)
      end = OffsetTime.of(endHour24, 0, 0, 0, ZoneOffset.UTC)
    })

  def binPackSchedule(daysToGenerate: Int, rooms: ArrayBuffer[Room], events: Set[(RequiredSession, Module)]): Option[List[ScheduledClass]] =
    binPackSchedule(daysToGenerate, rooms, events, Array(willFit(_, _, _)))

  def binPackSchedule(daysToGenerate: Int, rooms: ArrayBuffer[Room], events: Set[(RequiredSession, Module)],
                      filters: Seq[(Seq[RoomSchedule], Seq[(RequiredSession, Module)], RoomSchedule) => Seq[(RequiredSession, Module)]],
                      weights: Option[Seq[(Seq[RoomSchedule], (RequiredSession, Module), RoomSchedule) => Double]] =
                      None
                     ): Option[List[ScheduledClass]] = {
    val periods = for (i <- 1 until daysToGenerate + 1) yield getPeriodDefault(i)

    val schedule = rooms.flatMap(r => periods.map(new RoomSchedule(r, _)))

    // Events that have not been scheduled, shuffled to help distribute across the week
    var unProcEvents = Random.shuffle(events).to[ListBuffer]
    // Room schedules mapped to whether they contain space
    var scheduleMap = schedule.map((_ -> true)).toMap
    while (!unProcEvents.isEmpty) {
      // get the unfilled rooms
      val free = scheduleMap.filter(_._2).map(_._1).groupBy(_.period.calendar)

      if (free.isEmpty) {
        // If there are no free rooms, but there are events to be scheduled
        // return None, as failed to generate a schedule
        return None
      } else {
        // Get the free room that has the earliest unscheduled slot (day is included)
        val mostFree = free.minBy(_._1)._2.maxBy(_.timeRemaining)
        // Apply the filters to the unscheduled events, to find those that can be scheduled in the slot
        var validEvents = filters.foldLeft(unProcEvents) { (r, f) => f(schedule, r, mostFree).to[ListBuffer] }

        if (validEvents.isEmpty) {
          // no valid events can go in that slot
          /*TODO:
              Consider an approach that will assign free time, allowing the room to be scheduled later
              This will help prevent possible issues, where an early slot in a room is unscheduleable,
              which blocks the room being used later
           */
          scheduleMap += (mostFree -> false)
        } else {
          // Apply the weighting functions
          if (weights.isDefined)
            validEvents = validEvents.groupBy(s => weights.get.map(_ (schedule, s, mostFree)).sum).maxBy(_._1)._2
          // Get the longest events
          validEvents = validEvents.groupBy(_._1.durationInHours).maxBy(_._1)._2

          // Schedule the event
          mostFree + validEvents(0)
          unProcEvents -= validEvents(0)
        }

      }
    }

    Some(schedule.flatMap(_ ()).toList)
  }

  def willFit(currentSchedule: Seq[RoomSchedule], events: Seq[(RequiredSession, Module)], room: RoomSchedule): Seq[(RequiredSession, Module)] =
    events.filter(room.timeRemaining >= _._1.durationInHours)

  def noIntersect(schedule: Seq[RoomSchedule], events: Seq[(RequiredSession, Module)], room: RoomSchedule): Seq[(RequiredSession, Module)] = {
    // get Events that are running during the scheduleable time in the room
    val eventsSameDay = schedule.filter(_.period.calendar.compareTo(room.period.calendar) == 0).flatMap(_.events).filter(_._1.end.compareTo(room.durationPointer) > 0).groupBy(_._2._2.moduleName)
    events.filter(e => {
      val simultaneousModuleEvents = eventsSameDay.get(e._2.moduleName)
      if (simultaneousModuleEvents.isDefined) {
        // events from the module are running that day
        // check that they start after the current will end
        simultaneousModuleEvents.get.forall(s => s._1.start.compareTo(room.durationPointer.plus(e._1.durationInHours.toLong, ChronoUnit.HOURS)) > 0)
      } else {
        // No events from the events module are running that day
        true
      }
    })
  }

  class RoomSchedule(val room: Room, val period: Period) {
    var timeRemaining = (period.timePeriod.duration() / 60 / 60 / 1000).toFloat
    var durationPointer = period.timePeriod.start
    var events = mutable.HashMap[TimePeriod, (RequiredSession, Module)]()

    def +(event: (RequiredSession, Module)): Unit = {
      events += new TimePeriod(durationPointer, durationPointer.plus(event._1.durationInHours.toLong, ChronoUnit.HOURS)) -> event
      timeRemaining -= event._1.durationInHours
      durationPointer = durationPointer.plus(event._1.durationInHours.toLong, ChronoUnit.HOURS)
    }

    def apply() = events.map(e => new ScheduledClass(period, e._1, room, e._2._2.moduleName)).toList
  }

}
