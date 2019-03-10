package services.generator.persongenerator.staffgenerator

import entities.people.Staff
import services.generator.Generator

object StaffGenerator extends Generator[Staff] {
  override def gen(): Staff = new Staff(genInt, genStr, genInt, genStr, genStr, genStr, null, null)
}
