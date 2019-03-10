package services.generator.modulegenerator

import entities.module.ModuleSessionType
import services.generator.Generator

object ModuleSessionTypGenerator extends Generator[ModuleSessionType] {
  override def gen() = new ModuleSessionType(genInt, genStr, genStr)
}
