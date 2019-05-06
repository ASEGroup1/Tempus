package entities.module

import entities.timing.DayTimingConstraint

import scala.collection.mutable.ListBuffer

class ModuleTimingConstraint(
	var days: ListBuffer[(Int, DayTimingConstraint)] // Where Int is day number
)
