package entities.timing

import entities.Generator

import scala.collection.mutable.ListBuffer

object DayTimingConstraint extends Generator[DayTimingConstraint] {
	override def gen() = new DayTimingConstraint(genInt, genStr, null)
}

class DayTimingConstraint(
	var dayTimingConstraintId: Int,
	var dayTimingConstraintName: String,
	var dayTimingConstraints: ListBuffer[(TimePeriod, Boolean)] // The boolean means whether to schedule sessions during this time
)
