package services.generator.modulegenerator

import entities.module.ModuleTimetableSession
import services.generator.Generator

object ModuleTimetableSessionGenerator extends Generator[ModuleTimetableSession] {
  override def gen() = new ModuleTimetableSession(genInt, null, null, genInt)
}
