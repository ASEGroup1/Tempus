package services.generator.roomgenerator

import services.generator.Generator

import scala.util.Random

object RoomGenerator extends Generator[Int] {
  var i = 0
  override def generate(): Int = {i+=1; i}
}