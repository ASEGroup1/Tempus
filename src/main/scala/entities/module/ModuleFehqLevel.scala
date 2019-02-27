package entities.module

import entities.course.Course
import entities.people.Person

import scala.collection.mutable.ListBuffer

class ModuleFehqLevel {
	var baseModule: Module = _
	var fehqLevel: Int = _
	var prerequisite: ModuleFehqLevel = _
	var courseAvailability: ListBuffer[Course] = _
	var modulePeople: ListBuffer[(ModuleRole, Person)] = _
}
