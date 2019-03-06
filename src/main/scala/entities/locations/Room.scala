package entities.locations

import scala.collection.mutable.ListBuffer

class Room(
	var roomId: Int,
	var roomName: String,
	var roomType: RoomType,
	var roomCapacity: Int,
	var partitions: ListBuffer[RoomPartition]
)
