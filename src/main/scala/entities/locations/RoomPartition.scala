package entities.locations

import scala.collection.mutable.ListBuffer

class RoomPartition {
	var roomPartitionId: Int = _
	var room: Room = _
	var roomPartitionName: String = _
	var roomPartitionCapacity: Int = _
	var roomType: RoomType = _
	var features: ListBuffer[RoomFeature] = _
}
