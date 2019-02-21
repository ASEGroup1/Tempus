package services.generator.studentgenerator

import services.generator.Generator
import services.generator.eventgenerator.EventGenerator

object StudentGenerator extends Generator[Student] {

  override def generate(): Student = {
    Student(EventGenerator.generate(4))
  }
}
