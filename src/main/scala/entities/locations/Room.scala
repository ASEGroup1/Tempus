package entities.locations

import scala.collection.mutable.ListBuffer

class Room {
	var roomId: Int =_
	var roomName: String = _
	var roomType: RoomType = _
	var roomCapacity: Int = _
	var partitions: ListBuffer[RoomPartition] = _
}
