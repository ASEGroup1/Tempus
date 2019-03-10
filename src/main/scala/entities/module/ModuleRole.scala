package entities.module

import entities.Generator

object ModuleRole extends Generator[ModuleRole] {
	override def gen() = new ModuleRole(genInt, genStr, genStr)
}

class ModuleRole(
	var moduleRoleId: Int,
	var name: String,
	var description: String
)
