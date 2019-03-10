package services.generator.persongenerator.staffgenerator

import entities.people.Staff
import services.generator.Generator

object StaffGenerator extends Generator[Staff] {

  override def generate(): Staff = {
    val generatedValues = generate(Class[Int], Class[String], Class[Int], Class[String], Class[String], Class[String])

    new Staff(
      generatedValues(0).asInstanceOf[Int], generatedValues(1).asInstanceOf[String], generatedValues(2).asInstanceOf[Int], generatedValues(3).asInstanceOf[String],
      generatedValues(4).asInstanceOf[String], generatedValues(5).asInstanceOf[String], null, null
    )
  }
}
