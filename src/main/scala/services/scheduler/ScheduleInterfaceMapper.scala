package services.scheduler

import java.time.temporal.ChronoUnit

import entities.locations.Room
import entities.module.RequiredSession
import entities.timing.TimePeriod

class ScheduleInterfaceMapper(val room: Room, val event: Event, val period: TimePeriod, val scheduled: Boolean, val day: Int, val roomSchedule: RoomSchedule){

  var requiredSession: RequiredSession = _
  var weekNo: Int = _

  def this(roomSchedule: RoomSchedule, event: Event) = {
    this(roomSchedule.room, event, new TimePeriod(roomSchedule.durationPointer, roomSchedule.durationPointer.plus(event.duration.toLong, ChronoUnit.HOURS)), false, roomSchedule.period.calendar.getDayOfYear, roomSchedule)
  }

  def this(roomSchedule: RoomSchedule, slot: TimePeriod) = {
    this(roomSchedule.room, roomSchedule.events(slot), slot, true, roomSchedule.period.calendar.getDayOfYear, roomSchedule)
  }

  def this(room: Room, event: Event, period: TimePeriod, scheduled: Boolean, day: Int, roomSchedule: RoomSchedule, requiredSession: RequiredSession, weekNo: Int) = {
    this(room, event, period, scheduled, day, roomSchedule)
    this.requiredSession = requiredSession
    this.weekNo = weekNo
  }

  def withRequiredSession(session: (Int, RequiredSession)): ScheduleInterfaceMapper = new ScheduleInterfaceMapper(room, event, period, scheduled, day, roomSchedule, session._2, session._1)
}
