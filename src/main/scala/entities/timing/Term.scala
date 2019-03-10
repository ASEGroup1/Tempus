package entities.timing

import java.time.OffsetDateTime

import entities.Generator
import entities.module.Module

import scala.collection.mutable.ListBuffer

object Term extends Generator[Term] {
	override def gen() = new Term(genInt, genInt, genODT, genODT, null, null)
}

class Term(
	var termId: Int,
	var termNumber: Int,
	var startDate: OffsetDateTime,
	var endDate: OffsetDateTime,
	var termPeriods: ListBuffer[TermPeriod],
	var modules: ListBuffer[Module]
)
