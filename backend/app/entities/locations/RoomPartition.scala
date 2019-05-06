package entities.locations

import scala.collection.mutable.ListBuffer

class RoomPartition(
	var roomPartitionId: Int,
	var name: String,
	var capacity: Int,
	var roomPartitionType: RoomType,
	var features: ListBuffer[RoomFeature]
)
