package entities.module

import entities.Generator
import entities.course.Course
import entities.people.Person
import services.generator.modulegenerator.ModuleFehqLevel.genInt

import scala.collection.mutable.ListBuffer

object ModuleFehqLevel extends Generator[ModuleFehqLevel] {
	override def gen() = new ModuleFehqLevel(null, genInt, null, null, genInt(10, 500), null, null)
}

class ModuleFehqLevel(
	var baseModule: Module,
	var fehqLevel: Int,
	var prerequisites: ListBuffer[ModuleFehqLevel],
	var courseAvailability: ListBuffer[Course],
	var maxStudents: Int,
	var modulePeople: ListBuffer[(ModuleRole, Person)],
	var sessionStructure: ListBuffer[ModuleSessionStructure]
)
