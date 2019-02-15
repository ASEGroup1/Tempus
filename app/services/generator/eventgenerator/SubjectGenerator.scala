package services.generator.eventgenerator

import services.generator.Generator

import scala.util.Random

object SubjectGenerator extends Generator[String] {
  private[this] val subjects = Array("English", "Math", "Science", "Art")

  override def generate(): String = subjects(Random.nextInt(subjects.length - 1))
}
