package entities.locations

import java.util.regex.Matcher

import services.Utils

import scala.collection.mutable.ListBuffer

object Room {
  val intPatternString = "[0-9]+"

  def apply(m: Matcher): Room = {
    val capacity = if (m.group(3).matches(intPatternString)) m.group(3).toInt else -1

    new Room(m.group(1), null, capacity, Utils.toSnake(m.group(8)).contains("YES"), null)
  }
}

class Room(
	var roomName: String,
	var roomType: RoomType,
	var roomCapacity: Int,
	var disabledAccess: Boolean,
	var partitions: ListBuffer[RoomPartition]
)
