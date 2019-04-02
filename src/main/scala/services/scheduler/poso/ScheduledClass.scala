package services.scheduler.poso

import entities.timing.TimePeriod
import services.generator.eventgenerator.Event

class ScheduledClass(val day: Period, val time: TimePeriod, val room: entities.locations.Room, val className: String) {
	override def toString: String = {
		"%-12s %02d:%02d \t %02d:%02d \t %02d".format(className, time.start.getHour, time.start.getMinute,
			time.end.getHour, time.end.getMinute, day.calendar.getDayOfMonth)

	}

	def toJson = {
		s"""{ "room": "${room.roomName}",
        "event":"$className",
        "startHour":"${time.start.getHour}",
        "startMinute":"${time.start.getMinute}",
        "endHour" : "${time.end.getHour}",
        "endMinute" : "${time.end.getMinute}",
        "day" : "${day.calendar.getDayOfMonth}" }"""
	}
}
