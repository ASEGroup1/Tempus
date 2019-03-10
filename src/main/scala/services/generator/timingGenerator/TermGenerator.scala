package services.generator.timingGenerator

import entities.timing.Term
import services.generator.Generator

object TermGenerator extends Generator[Term] {
  override def gen() = new Term(genInt, genInt, genODT, genODT, null, null)
}
