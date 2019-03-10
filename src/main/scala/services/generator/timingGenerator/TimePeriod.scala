package services.generator.timingGenerator

import entities.timing.TimePeriod
import services.generator.Generator

object TimePeriod extends Generator[TimePeriod] {
  override def gen() = new TimePeriod(genInt, genOT, genOT)
}
