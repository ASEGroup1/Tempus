package services.generator.modulegenerator

import entities.module.ModuleFehqLevel
import services.generator.Generator

object ModuleFehqLevel extends Generator[ModuleFehqLevel] {
  override def gen() = new ModuleFehqLevel(null, genInt, null, null, genInt(10, 500), null, null)
}
