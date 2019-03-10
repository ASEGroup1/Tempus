package entities.locations

import entities.Generator

import scala.collection.mutable.ListBuffer

object Building extends Generator[Building] {
	override def gen = new Building(genInt, genStr, null)
}

class Building(
	var buildingId: Int,
	var name: String,
	var floors: ListBuffer[BuildingFloor]
)
