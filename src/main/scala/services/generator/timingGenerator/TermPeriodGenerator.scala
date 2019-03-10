package services.generator.timingGenerator

import entities.timing.TermPeriod
import services.generator.Generator

object TermPeriodGenerator extends Generator[TermPeriod] {
  override def gen() = new TermPeriod(genInt, genInt, genODT, genODT)
}
