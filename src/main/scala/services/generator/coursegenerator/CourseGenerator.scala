package services.generator.coursegenerator

import entities.course.Course
import services.generator.Generator

class CourseGenerator extends Generator[Course] {
  override def gen() = new Course(genInt, genStr, null, null, null)
}
