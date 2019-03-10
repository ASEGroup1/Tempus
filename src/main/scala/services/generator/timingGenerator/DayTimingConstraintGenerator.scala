package services.generator.timingGenerator

import entities.timing.DayTimingConstraint
import services.generator.Generator

object DayTimingConstraintGenerator extends Generator[DayTimingConstraint] {
  override def gen() = new DayTimingConstraint(genInt, genStr, null)
}
