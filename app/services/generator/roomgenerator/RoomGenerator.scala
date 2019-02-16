package services.generator.roomgenerator

import services.generator.Generator

import scala.util.Random

object RoomGenerator extends Generator[Int] {
  override def generate(): Int = Random.nextInt
}