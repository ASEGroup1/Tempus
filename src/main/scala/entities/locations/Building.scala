package entities.locations

import scala.collection.mutable.ListBuffer

class Building(
	var buildingId: Int,
	var name: String,
	var floors: ListBuffer[BuildingFloor]
)
