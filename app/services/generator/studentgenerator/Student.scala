package services.generator.studentgenerator

import services.generator.eventgenerator.Event

object Student {
  def apply(requiredEvents: IndexedSeq[Event]): Student = new Student(requiredEvents)
}

class Student(val requiredEvents: IndexedSeq[Event]) {
  override def toString = requiredEvents.mkString("[", ",", "]")
}