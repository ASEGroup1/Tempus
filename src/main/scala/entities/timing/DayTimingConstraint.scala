package entities.timing

import scala.collection.mutable.ArrayBuffer

class DayTimingConstraint {
	var dayTimingConstraintId: Int = _
	var dayTimingConstraintName: String = _
	var dayTimingConstraints: ArrayBuffer[(TimePeriod, Boolean)] = _ // The boolean means whether to schedule sessions during this time

}
