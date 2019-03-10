package services.generator.coursegenerator

import entities.course.CourseRole
import services.generator.Generator

class CourseRoleGenerator extends Generator[CourseRole] {
  override def gen() = new CourseRole(genInt, genStr, genStr)
}
