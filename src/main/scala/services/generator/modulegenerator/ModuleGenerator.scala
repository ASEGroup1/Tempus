package services.generator.modulegenerator

import entities.module.Module
import services.generator.Generator

object ModuleGenerator extends Generator[Module] {
  override def gen() = new Module(genInt, genStr, genStr, genStr, null, null)
}
