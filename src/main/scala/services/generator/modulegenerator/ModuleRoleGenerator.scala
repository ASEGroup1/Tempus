package services.generator.modulegenerator

import entities.module.ModuleRole
import services.generator.Generator

object ModuleRoleGenerator extends Generator[ModuleRole] {
  override def gen() = new ModuleRole(genInt, genStr, genStr)
}
