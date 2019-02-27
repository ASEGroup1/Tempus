package services.scheduler.poso

import services.generator.eventgenerator.Event

class ScheduledClass(val day: Period, val time: Duration, val room: Room, val event: Event) {
  override def toString: String = {
    "%-5d %-12s %02d:%02d \t %02d:%02d \t %02d".format(room.id, event.name, time.start.getHourOfDay, time.start.getMinuteOfHour,
      time.end.getHourOfDay, time.end.getMinuteOfHour, day.calendar.getDayOfMonth)
  }

  def toJson = {
    s"""{ "room": "${room.id}",
        "event":"${event.name}",
        "startHour":"${time.start.getHourOfDay}",
        "startMinute":"${time.start.getMinuteOfHour}",
        "endHour" : "${time.end.getHourOfDay}",
        "endMinute" : "${time.end.getMinuteOfHour}",
        "day" : "${day.calendar.getDayOfMonth}" }"""
  }
}
