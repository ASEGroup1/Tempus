package entities.locations

import scala.collection.mutable.ListBuffer

class Building {
	var buildingId: Int = _
	var name: String = _
	var floors: ListBuffer[BuildingFloor] = _
}
