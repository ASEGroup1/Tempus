package entities.people

import entities.course.{Course, CourseRole}
import entities.module.{Module, ModuleFehqLevel, ModuleRole}

import scala.collection.mutable.ListBuffer

abstract class Person(
                       var personId: Int,
                       var firstName: String,
                       var lastName: String,
                       var otherNames: String,
                       var courses: ListBuffer[(CourseRole, Course)],
                       var modules: Set[(ModuleRole, Module)]
                     )
