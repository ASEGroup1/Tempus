package services.scheduler.poso

import entities.timing.TimePeriod
import services.generator.eventgenerator.Event

class ScheduledClass(val day: Period, val time: TimePeriod, val room: Room, val event: Event) {
  override def toString: String = {
    "%-5d %-12s %02d:%02d \t %02d:%02d \t %02d".format(room.id, event.name, time.start.getHour, time.start.getMinute,
      time.end.getHour, time.end.getMinute, day.calendar.getDayOfMonth)
  }
}
