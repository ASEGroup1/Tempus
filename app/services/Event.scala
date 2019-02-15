package services

import services.EventType.EventType

class Event(val name:String, val eventType:EventType, val occurrencesPerWeek: Double)

object EventType extends Enumeration {
  type EventType = Value
  val LECTURE, SEMINAR, LAB, OTHER = Value
}