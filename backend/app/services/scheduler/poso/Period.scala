package services.scheduler.poso

import entities.timing.TimePeriod
import java.time.OffsetDateTime

@SerialVersionUID(100L)
class Period(val calendar: OffsetDateTime, val timePeriod: TimePeriod) extends Serializable
