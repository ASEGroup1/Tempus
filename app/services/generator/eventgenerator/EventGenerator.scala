package services.generator.eventgenerator

import org.joda.time.LocalTime
import services.generator.Generator

import scala.util.Random

object EventGenerator extends Generator[Event] {
  override def generate(): Event = Event(SubjectGenerator.generate(), EventType.values.toList(Random.nextInt(EventType.values.size - 1)), new LocalTime(Random.nextInt(24), Random.nextInt(60)))
}
