package entities.locations

import scala.collection.mutable.ArrayBuffer

class Building {
	var buildingId: Int = _
	var name: String = _
	var floors: ArrayBuffer[BuildingFloor] = _
}
