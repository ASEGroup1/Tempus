package services.generator.modulegenerator

import entities.module.ModuleTimetable
import services.generator.Generator

object ModuleTimetableGenerator extends Generator[ModuleTimetable]{
  override def gen() = new ModuleTimetable(genInt, null)
}
