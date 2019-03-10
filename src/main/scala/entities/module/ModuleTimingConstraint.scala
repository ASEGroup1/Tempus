package entities.module

import entities.Generator
import entities.timing.DayTimingConstraint

import scala.collection.mutable.ListBuffer

object ModuleTimingConstraint extends Generator[ModuleTimingConstraint]{
	override def gen() = new ModuleTimingConstraint(null)
}

class ModuleTimingConstraint(
	var days: ListBuffer[(Int, DayTimingConstraint)] // Where Int is day number
)
