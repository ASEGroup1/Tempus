package services.generator.eventgenerator

import org.joda.time.LocalTime
import services.generator.Generator

import scala.util.Random

object EventGenerator extends Generator[Event] {
  val durations = Array(0, 30)

  override def generate(): Event = {
    val duration = (Random.nextInt(8) + 1) * 30
    Event(SubjectGenerator.generate(), EventType.values.toList(Random.nextInt(EventType.values.size - 1)), new LocalTime(Math.floorDiv(duration, 60), duration % 60))
  }
}
