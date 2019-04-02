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
  
	def this(start: OffsetTime,	end: OffsetTime) = this(0, start, end)

	def duration(): Int = {end.get(ChronoField.MILLI_OF_DAY) - start.get(ChronoField.MILLI_OF_DAY)}
	def length = end.getHour - start.getHour
}
