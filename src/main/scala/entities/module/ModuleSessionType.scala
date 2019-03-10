package entities.module

import entities.Generator

object ModuleSessionType extends Generator[ModuleSessionType] {
	override def gen() = new ModuleSessionType(genInt, genStr, genStr)
}

class ModuleSessionType(
	var moduleSessionTypeId: Int,
	var name: String,
	var description: String
)
