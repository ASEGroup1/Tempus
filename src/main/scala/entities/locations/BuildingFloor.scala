package entities.locations

import entities.Generator

import scala.collection.mutable.ListBuffer

object BuildingFloor extends Generator[BuildingFloor] {
	override def gen() = new BuildingFloor(genInt, genInt(0, 5), null)
}

class BuildingFloor(
	var floorId: Int,
	var floorLevel: Int,
	var rooms: ListBuffer[Room]
)
