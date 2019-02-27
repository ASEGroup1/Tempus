package entities.people

import entities.Course

import scala.collection.mutable.ArrayBuffer

class Person {

	var personId: Int = _
	var firstName: String = _
	var lastName: String = _
	var otherNames: String = _
	var courses: ArrayBuffer[Course] = _
}
