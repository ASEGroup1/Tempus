package services.scheduler.poso

import org.joda.time.LocalTime

class Duration(val start: LocalTime, val end: LocalTime) {
  def duration(): Int = end.getMillisOfDay - start.getMillisOfDay

  def this(startHour: Int, endHour:Int) =
    this(new LocalTime(startHour, 0), new LocalTime(endHour, 0))
}