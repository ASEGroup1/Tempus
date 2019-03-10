package services.generator.timingGenerator

import entities.timing.AcademicYear
import services.generator.Generator

object AcademicYearGenerator extends Generator[AcademicYear] {
  override def gen() = new AcademicYear(genInt, genStr, genODT, genODT, null)
}
