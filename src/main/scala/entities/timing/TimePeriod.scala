package entities.timing

import java.time.OffsetTime

class TimePeriod(
	var timePeriodId: Int,
	var start: OffsetTime,
	var end: OffsetTime
)
