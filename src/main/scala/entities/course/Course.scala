package entities.course

import entities.School
import entities.people.Person

import scala.collection.mutable.ListBuffer

class Course {
  var courseId: Int = _
  var courseName: String = _
  var school: School = _
  var coursePeople: ListBuffer[(CourseRole, Person)] = _
}
