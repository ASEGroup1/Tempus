package services.generator.studentgenerator

import services.generator.eventgenerator.Event

object Student {
  def apply(requiredEvents: Seq[Event]): Student = new Student(requiredEvents)
}

class Student(val requiredEvents: Seq[Event]) {
  override def toString = requiredEvents.mkString("[", ",", "]")
}