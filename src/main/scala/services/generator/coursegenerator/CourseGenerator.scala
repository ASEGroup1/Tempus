package services.generator.coursegenerator

import entities.course.Course
import services.generator.Generator

object CourseGenerator extends Generator[Course] {
  override def gen() = new Course(genInt, genStr, null, null, null)
}
