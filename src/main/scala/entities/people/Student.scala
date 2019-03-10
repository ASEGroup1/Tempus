package entities.people

import entities.course.{Course, CourseRole}
import entities.module.{ModuleFehqLevel, ModuleRole}

import scala.collection.mutable.ListBuffer

class Student(var studentId: Int, var course: Course, var currentFehqLevelCompleted: Int, var academicAdvisor: Staff,
              personId: Int, firstName: String, lastName: String, otherNames: String,
              courses: ListBuffer[(CourseRole, Course)], modules: ListBuffer[(ModuleRole, ModuleFehqLevel)]
             ) extends Person(personId, firstName, lastName, otherNames, courses, modules)
