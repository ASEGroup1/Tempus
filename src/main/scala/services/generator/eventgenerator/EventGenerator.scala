package services.generator.eventgenerator

import java.time.{OffsetTime, ZoneOffset}

import services.generator.Generator

import scala.util.Random

object EventGenerator extends Generator[Event] {
  val durations = Array(0, 30)

  override def gen(): Event = {
    val duration = (Random.nextInt(8) + 1) * 30
    Event(SubjectGenerator.gen(), EventType.values.toList(Random.nextInt(EventType.values.size - 1)), OffsetTime.of(Math.floorDiv(duration, 60), duration % 60,0,0,ZoneOffset.UTC))
  }
}
