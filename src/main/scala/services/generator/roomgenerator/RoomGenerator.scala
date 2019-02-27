package services.generator.roomgenerator

import java.util.UUID

import services.generator.Generator

object RoomGenerator extends Generator[String] {
  override def generate(): String = UUID.randomUUID.toString
}