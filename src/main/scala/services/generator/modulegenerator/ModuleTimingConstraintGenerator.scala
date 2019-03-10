package services.generator.modulegenerator

import entities.module.ModuleTimingConstraint
import services.generator.Generator

object ModuleTimingConstraintGenerator extends Generator[ModuleTimingConstraint]{
  override def gen() = new ModuleTimingConstraint(null)
}
