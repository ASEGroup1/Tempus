package services.scheduler.poso

import services.generator.eventgenerator.Event

class ScheduledClass(val day: Period, val time: Duration, val room: Room, val event: Event) {
  override def toString: String = {
    "%-12s %02d:%02d \t %02d:%02d \t %02d".format( event.name, time.start.getHourOfDay, time.start.getMinuteOfHour,
      time.end.getHourOfDay, time.end.getMinuteOfHour, day.calendar.getDayOfMonth)
  }
}
