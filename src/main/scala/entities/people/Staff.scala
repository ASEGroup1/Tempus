package entities.people

import entities.course.{Course, CourseRole}
import entities.module.{ModuleFehqLevel, ModuleRole}

import scala.collection.mutable.ListBuffer

class Staff(var staffId: Int, var jobTitle: String, personId: Int, firstName: String, lastName: String, otherNames: String,
							courses: ListBuffer[(CourseRole, Course)], modules: ListBuffer[(ModuleRole, ModuleFehqLevel)]
						 ) extends Person(personId, firstName, lastName, otherNames, courses, modules)