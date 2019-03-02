package entities.locations

import scala.collection.mutable.ListBuffer

class BuildingFloor {
	var floorId: Int = _
	var floorLevel: Int = _
	var rooms: ListBuffer[Room] = _
}
