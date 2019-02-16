package services.generator.eventgenerator

import org.joda.time.LocalTime
import services.generator.eventgenerator.EventType.EventType

object Event {
  def apply(name: String, eventType: EventType, occurrencesPerWeek: LocalTime): Event = new Event(name, eventType, occurrencesPerWeek)
}

class Event(val name:String, val eventType:EventType, val hours: LocalTime) {
  override def toString: String = s"$name - $eventType - $hours"
}

object EventType extends Enumeration {
  type EventType = Value
  val LECTURE, SEMINAR, LAB, OTHER = Value
}