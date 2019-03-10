package services.generator.eventgenerator

import services.generator.Generator

import scala.util.Random

object SubjectGenerator extends Generator[String] {
  private[this] val subjects = (1 to 10).map("Module " + _)

  override def gen(): String = subjects(Random.nextInt(subjects.length - 1))
}
