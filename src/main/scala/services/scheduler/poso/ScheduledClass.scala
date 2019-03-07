package services.scheduler.poso

import entities.timing.TimePeriod
import services.generator.eventgenerator.Event

class ScheduledClass(val day: Period, val time: TimePeriod, val room: Room, val event: Event) {
  override def toString: String = {
<<<<<<< HEAD
    "%-12s %02d:%02d \t %02d:%02d \t %02d".format( event.name, time.start.getHourOfDay, time.start.getMinuteOfHour,
      time.end.getHourOfDay, time.end.getMinuteOfHour, day.calendar.getDayOfMonth)
=======
    "%-5d %-12s %02d:%02d \t %02d:%02d \t %02d".format(room.id, event.name, time.start.getHour, time.start.getMinute,
      time.end.getHour, time.end.getMinute, day.calendar.getDayOfMonth)
>>>>>>> T9-AddScalaOrmObjects
  }

  def toJson = {
    s"""{ "room": "${room.name}",
        "event":"${event.name}",
        "startHour":"${time.start.getHourOfDay}",
        "startMinute":"${time.start.getMinuteOfHour}",
        "endHour" : "${time.end.getHourOfDay}",
        "endMinute" : "${time.end.getMinuteOfHour}",
        "day" : "${day.calendar.getDayOfMonth}" }"""
  }
}
