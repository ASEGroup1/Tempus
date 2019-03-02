package entities.locations

import scala.collection.mutable.ListBuffer

class RoomPartition {
	var roomPartitionId: Int = _
	var name: String = _
	var capacity: Int = _
	var roomPartitionType: RoomType = _
	var features: ListBuffer[RoomFeature] = _
}
