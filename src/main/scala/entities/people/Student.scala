package entities.people

import entities.course.Course
import services.generator.Generator


object Student extends Generator[Student] {
	override def generate(): Student = {
		null
	}
}

class Student extends Person {
	var studentId: Int = _
	var course: Course = _
	var currentFehqLevelCompleted: Int = _
	var academicAdvisor: Staff = _
}
