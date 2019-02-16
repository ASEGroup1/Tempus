package services.generator.eventgenerator

import org.joda.time.LocalTime
import services.generator.eventgenerator.EventType.EventType

object Event {
  def apply(name: String, eventType: EventType, duration: LocalTime): Event = new Event(name, eventType, duration)
}

class Event(val name:String, val eventType:EventType, val duration: LocalTime) {
  override def toString: String = s"$name - $eventType - $duration"
}

object EventType extends Enumeration {
  type EventType = Value
  val LECTURE, SEMINAR, LAB, OTHER = Value
}