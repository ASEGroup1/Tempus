package entities.locations

import scala.collection.mutable.ListBuffer

class BuildingFloor(
	var floorId: Int,
	var floorLevel: Int,
	var rooms: ListBuffer[Room]
)
