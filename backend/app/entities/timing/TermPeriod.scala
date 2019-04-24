package entities.timing

import java.time.OffsetDateTime

class TermPeriod(
	var termPeriodId: Int,
	var termPeriodNo: Int,
	var start: OffsetDateTime,
	var end: OffsetDateTime
)
