package entities.timing

import scala.collection.mutable.ListBuffer

class DayTimingConstraint(
	var dayTimingConstraintId: Int,
	var dayTimingConstraintName: String,
	var dayTimingConstraints: ListBuffer[(TimePeriod, Boolean)] // The boolean means whether to schedule sessions during this time

)
