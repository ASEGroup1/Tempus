package entities.module

import entities.course.Course
import entities.people.Person

import scala.collection.mutable.ListBuffer

class ModuleFehqLevel {
	var baseModule: Module = _
	var fehqLevel: Int = _
	var prerequisites: ListBuffer[ModuleFehqLevel] = _
	var courseAvailability: ListBuffer[Course] = _
	var maxStudents: Int = _
	var modulePeople: ListBuffer[(ModuleRole, Person)] = _
	var sessionStructure: ListBuffer[ModuleSessionStructure] = _
}
