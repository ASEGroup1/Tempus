package entities.timing

import java.time.OffsetDateTime

import entities.module.Module

import scala.collection.mutable.ListBuffer

class Term {
	var termId: Int = _
	var termNumber: Int = _
	var startDate: OffsetDateTime = _
	var endDate: OffsetDateTime = _
	var termPeriods: ListBuffer[TermPeriod] = _
	var modules: ListBuffer[Module] = _
}
