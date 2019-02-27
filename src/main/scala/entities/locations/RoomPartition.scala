package entities.locations

import scala.collection.mutable.ListBuffer

class RoomPartition {
	var roomPartitionId: Int = _
	var room: Room = _
	var name: String = _
	var capacity: Int = _
	var partitionType: RoomType = _
	var features: ListBuffer[RoomFeature] = _
}
