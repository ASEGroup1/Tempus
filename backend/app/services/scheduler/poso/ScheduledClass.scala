package services.scheduler.poso

import entities.timing.TimePeriod

@SerialVersionUID(1000L)
class ScheduledClass(val day: Period, val time: TimePeriod, val room: entities.locations.Room, val className: String) extends Serializable {
	override def toString: String = {
		"%-12s %02d:%02d \t %02d:%02d \t %02d".format(className, time.start.getHour, time.start.getMinute,
			time.end.getHour, time.end.getMinute, day.calendar.getDayOfMonth)
	}

	def canEqual(other: Any): Boolean = other.isInstanceOf[ScheduledClass]

	override def equals(other: Any): Boolean = other match {
		case that: ScheduledClass =>
			(that canEqual this) &&
				day == that.day &&
				time == that.time &&
				room == that.room &&
				className == that.className
		case _ => false
	}

	override def hashCode(): Int = {
		val state = Seq(day, time, room, className)
		state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
	}
}
