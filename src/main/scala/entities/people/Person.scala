package entities.people

import entities.course.{Course, CourseRole}
import entities.module.{ModuleFehqLevel, ModuleRole}

import scala.collection.mutable.ListBuffer

abstract class Person {

	var personId: Int = _
	var firstName: String = _
	var lastName: String = _
	var otherNames: String = _
	var courses: ListBuffer[(CourseRole, Course)] = _
	var modules: ListBuffer[(ModuleRole, ModuleFehqLevel)] = _
}
