package services.generator.modulegenerator

import entities.module.ModuleSessionStructure
import services.generator.Generator

object ModuleSessionStructureGenerator extends Generator[ModuleSessionStructure]{
  override def gen() = new ModuleSessionStructure(genInt, genInt, null, genInt(0, 18), genInt(5, 100))
}
