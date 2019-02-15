package services.generator.eventgenerator

import services.generator.Generator

import scala.util.Random

object EventGenerator extends Generator[Event] {
  override def generate(): Event = Event(SubjectGenerator.generate(), EventType.values.toList(Random.nextInt(EventType.values.size - 1)), Random.nextInt(4) + 1)
}
