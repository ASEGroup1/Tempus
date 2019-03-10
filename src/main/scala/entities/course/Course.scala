package entities.course

import entities.{Generator, School}
import entities.module.ModuleFehqLevel
import entities.people.Person

import scala.collection.mutable.ListBuffer

object Course extends Generator[Course] {
	override def gen() = new Course(genInt, genStr, null, null, null)
}

class Course(
	val courseId: Int,
	var courseName: String,
	var school: School,
	var coursePeople: ListBuffer[(CourseRole, Person)],
	var moduleAvailability: ListBuffer[ModuleFehqLevel],
)
