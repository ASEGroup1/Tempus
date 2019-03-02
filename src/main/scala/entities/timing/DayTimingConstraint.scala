package entities.timing

import scala.collection.mutable.ListBuffer

class DayTimingConstraint {
	var dayTimingConstraintId: Int = _
	var dayTimingConstraintName: String = _
	var dayTimingConstraints: ListBuffer[(TimePeriod, Boolean)] = _ // The boolean means whether to schedule sessions during this time

}
