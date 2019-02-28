package entities.people

import entities.course.{Course, CourseRole}

import scala.collection.mutable.ListBuffer

class Person {

	var personId: Int = _
	var firstName: String = _
	var lastName: String = _
	var otherNames: String = _
	var courses: ListBuffer[(CourseRole, Course)] = _
}
