package entities.timing

import java.time.OffsetTime
import java.time.temporal.ChronoField

class TimePeriod(
	var timePeriodId: Int,
	var start: OffsetTime,
	var end: OffsetTime
)
{
	def this() = this(0,null,null) // Adds empty constructor

	def duration(): Int = if(start != null && end  != null) end.get(ChronoField.MILLI_OF_DAY) - start.get(ChronoField.MILLI_OF_DAY) else 0

	def length = end.getHour - start.getHour
}
