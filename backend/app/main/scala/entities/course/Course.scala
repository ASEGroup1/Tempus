package entities.course

import entities.School
import entities.module.ModuleFehqLevel
import entities.people.Person

import scala.collection.mutable.ListBuffer

class Course(
	val courseId: Int,
	var courseName: String,
	var school: School,
	var coursePeople: ListBuffer[(CourseRole, Person)],
	var moduleAvailability: ListBuffer[ModuleFehqLevel],
)
