package services.generator.persongenerator.studentgenerator

import entities.people.Student
import services.generator.Generator

object StudentGenerator extends Generator[Student] {

  override def generate(): Student = {
    val generatedValues = generate(Class[Int], Class[Int], Class[Int], Class[String], Class[String], Class[String])

    new Student(
      generatedValues(0).asInstanceOf[Int], null, generatedValues(1).asInstanceOf[Int], null, generatedValues(2).asInstanceOf[Int],
      generatedValues(3).asInstanceOf[String], generatedValues(4).asInstanceOf[String], generatedValues(5).asInstanceOf[String], null, null
    )
  }
}
