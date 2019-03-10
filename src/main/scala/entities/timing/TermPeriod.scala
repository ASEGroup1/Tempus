package entities.timing

import java.time.OffsetDateTime

import entities.Generator

object TermPeriod extends Generator[Term] {
	override def gen() = new Term(genInt, genInt, genODT, genODT, null, null)
}

class TermPeriod(
	var termPeriodId: Int,
	var termPeriodNo: Int,
	var start: OffsetDateTime,
	var end: OffsetDateTime
)
