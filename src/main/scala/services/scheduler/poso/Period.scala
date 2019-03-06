package services.scheduler.poso

import entities.timing.TimePeriod
import java.time.OffsetDateTime

class Period(val calendar: OffsetDateTime, val timePeriod: TimePeriod)
