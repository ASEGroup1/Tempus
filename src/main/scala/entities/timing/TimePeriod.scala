package entities.timing

import java.time.OffsetTime
import java.time.temporal.ChronoField

import entities.Generator
import services.generator.timingGenerator.TimePeriod.{genInt, genOT}

object TimePeriod extends Generator[TimePeriod] {
	override def gen() = new TimePeriod(genInt, genOT, genOT)
}

class TimePeriod(
	var timePeriodId: Int,
	var start: OffsetTime,
	var end: OffsetTime
)
{
	def this() = this(0,null,null) // Adds empty constructor

	def duration(): Int = {end.get(ChronoField.MILLI_OF_DAY) - start.get(ChronoField.MILLI_OF_DAY)}

	def length = end.getHour - start.getHour
}
