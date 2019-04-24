package entities.timing

import java.time.OffsetDateTime

import entities.module.Module

import scala.collection.mutable.ListBuffer

class Term(
	var termId: Int,
	var termNumber: Int,
	var startDate: OffsetDateTime,
	var endDate: OffsetDateTime,
	var termPeriods: ListBuffer[TermPeriod],
	var modules: ListBuffer[Module]
)
