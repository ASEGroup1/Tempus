package services.scheduler.poso

import org.joda.time.DateTime

class Period(val calendar: DateTime, val duration: Duration) {
  override def equals(obj: Any): Boolean = {
    if (obj.isInstanceOf[Period]) {
      val per = obj.asInstanceOf[Period]
      per.calendar == calendar && per.duration == duration
    } else {
      false
    }
  }

  def intersect(period: Period): Boolean =
    period.calendar == calendar && duration.overlap(period.duration)
}