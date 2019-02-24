package services.scheduler.poso

import org.joda.time.LocalTime

class Duration(val start: LocalTime, val end: LocalTime) {
  def duration(): Int = {
    end.getMillisOfDay - start.getMillisOfDay
  }

  def durationMins: Int =
    duration() / 60000

  override def equals(obj: Any): Boolean = {
    if (obj.isInstanceOf[Duration]) {
      val dur = obj.asInstanceOf[Duration]
      dur.start == start && dur.end == dur.end
    } else {
      false
    }
  }

  def overlap(duration: Duration): Boolean =
    start.getMillisOfDay <= duration.end.getMillisOfDay && end.getMillisOfDay >= duration.start.getMillisOfDay

  def canContain(duration: Duration): Boolean =
    start.getMillisOfDay <= duration.start.getMillisOfDay && end.getMillisOfDay >= duration.end.getMillisOfDay
}