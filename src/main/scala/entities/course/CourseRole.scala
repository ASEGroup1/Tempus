package entities.course

import entities.Generator

object CourseRole extends Generator[CourseRole] {
	override def gen() = new CourseRole(genInt, genStr, genStr)
}

class CourseRole(
	var courseRoleId: Int,
	var courseRoleName: String,
	var courseRoleDescription: String
)
