package services.generator.persongenerator.studentgenerator

import entities.people.Student
import services.generator.Generator

object StudentGenerator extends Generator[Student] {
  override def gen(): Student = new Student(genInt, null, genInt, null, genInt, genStr, genStr, genStr, null, null)
}
