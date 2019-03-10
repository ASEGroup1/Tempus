package entities.module

import entities.Generator

object ModuleSessionStructure extends Generator[ModuleSessionStructure]{
	override def gen() = new ModuleSessionStructure(genInt, genInt, null, genInt(0, 18), genInt(5, 100))
}

class ModuleSessionStructure(
	var moduleSessionStructureId: Int,
	var weekNo: Int,
	var sessionType: ModuleSessionType,
	var noOfSessions: Int,
	var maxSessionSize: Int
)
