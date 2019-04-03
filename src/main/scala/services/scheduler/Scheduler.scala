package services.scheduler

import java.time.temporal.ChronoUnit
import java.time.{OffsetDateTime, OffsetTime, ZoneOffset}

import entities.locations.Room
import entities.module.{Module, RequiredSession}
import entities.timing.TimePeriod
import services.parser.TimeTableParser
import services.scheduler.poso.{Period, ScheduledClass}
import services.sussexroomscraper.SussexRoomScraper
import services.generator.eventgenerator.Event
import services.scheduler.Scheduler.RoomSchedule

import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, ListBuffer}

object Scheduler {
  def getPeriodDefault(dayOfMonth: Int) = getPeriod(dayOfMonth, 1, 8, 20)

  def getPeriod(dayOfMonth: Int, monthOfYear: Int, beginHour24: Int, endHour24: Int) =
    new Period(OffsetDateTime.parse(s"2019-0$monthOfYear-0${dayOfMonth}T00:00:00+00:00"), new TimePeriod {
      start = OffsetTime.of(beginHour24, 0, 0, 0, ZoneOffset.UTC)
      end = OffsetTime.of(endHour24, 0, 0, 0, ZoneOffset.UTC)
    })

  def binPackSchedule(daysToGenerate: Int, rooms: ArrayBuffer[Room], events: Set[(RequiredSession, Module)]): Option[List[ScheduledClass]] =
    binPackSchedule(daysToGenerate, rooms, events, Array(willFit(_,_,_), noIntersect(_,_,_)))

  def binPackSchedule(daysToGenerate: Int, rooms: ArrayBuffer[Room], events: Set[(RequiredSession, Module)],
                      filters: Seq[(Seq[RoomSchedule], Seq[(RequiredSession, Module)], RoomSchedule) => Seq[(RequiredSession, Module)]],
                      weights: Option[Seq[(Seq[RoomSchedule], (RequiredSession, Module), RoomSchedule) => Double]] =
                      None
                     ): Option[List[ScheduledClass]] = {
    val periods = for (i <- 1 until daysToGenerate + 1) yield getPeriodDefault(i)

    val schedule = rooms.flatMap(r => periods.map(new RoomSchedule(r, _)))
    val eventDuration = events.map(_._1).toList.map(_.durationInHours).sum
    val schedulableTime = schedule.map(_.timeRemaining).sum
    val timeDetla = schedulableTime - eventDuration
    // untill events are empty
    // find smallest room
    // get list of events that pass filters
    // order filtered events by (weight, duration)
    // put first element into that slot
    var unProcEvents = events.to[ListBuffer]
    // (day room -> can be scheduled)
    var scheduleMap = schedule.map((_ -> true)).toMap
    while(!unProcEvents.isEmpty){
      // get the room that has the earliest unscehduled slot (day is included)
      val free = scheduleMap.filter(_._2).map(_._1).groupBy(_.period.calendar)
      if (free.isEmpty){
        val available = scheduleMap.filter(_._2)
        val unavailableTimeLeft = scheduleMap.filter(!_._2).map(_._1).groupBy(_.timeRemaining)
        val timeToSchedule = unProcEvents.map(_._1.durationInHours).sum
        val timeLeft = schedule.map(_.timeRemaining).sum
        val missingTime = timeLeft - timeToSchedule
        val scheduledEvents = schedule.flatMap(_.events)
        return None
      }else {
        val mostFree = free.minBy(_._1)._2.maxBy(_.timeRemaining)
        val validEvents = filters.foldLeft(unProcEvents){(r,f) => f(schedule, r, mostFree).to[ListBuffer]}
        if (weights.isDefined)
          validEvents.sortBy(s => (weights.get.map(_(schedule, s, mostFree)).sum, s._1.durationInHours)).reverse
        else
          validEvents.sortBy(_._1.durationInHours).reverse
        if (validEvents.isEmpty){
          // no valid events can go in that slot
          scheduleMap += (mostFree -> false)
        }else{
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
    // get Events that are running during the schedulable time in the room
    val tmp1 = schedule.filter(_.period.calendar.compareTo(room.period.calendar) == 0).flatMap(_.events)
    val targetTime = room.durationPointer
    val tmp2 = tmp1.filter(_._1.end.compareTo(targetTime)>0)
    val eventsSameDay = tmp2.groupBy(_._2._2.moduleName)
    //
    val res = events.filter(e => {
      val simultaniousModuleEvents = eventsSameDay.get(e._2.moduleName)
      if (simultaniousModuleEvents.isDefined){
        // events from the module are running that day
        // check that they start after the current will end
        val res = simultaniousModuleEvents.get.forall(s => s._1.start.compareTo(room.durationPointer.plus(e._1.durationInHours.toLong, ChronoUnit.HOURS))>0)
        res
      }else{
        // No events from the events module are running that day
        true
      }
    })
    // remove those that finished before current time
    // events whoose names are not in the list are fine
    // events whoose names are in list need to be checked too see if they end before conflicts start


    res
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
