package services.scheduler.poso

import org.joda.time.LocalTime

class Duration(val start: LocalTime, val end: LocalTime) {
  def duration(): Int = {
    end.getMillisOfDay - start.getMillisOfDay
  }
}