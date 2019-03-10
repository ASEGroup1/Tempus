package entities.locations

import java.util.regex.Matcher

import scala.collection.mutable.ListBuffer

object Room {
  val intPatternString = "[0-9]+"

  def apply(m: Matcher): Room = {
    val capacity = if (m.group(3).matches(intPatternString)) m.group(3).toInt else -1

    new Room(m.group(1), getRoomType(m.group(2)), capacity, m.group(8).toLowerCase().replace(" ", "") == "yes", null)
  }

  val LecturePattern = ".*[L|l]ecture.*".r
  val SeminarPattern = ".*[S|s]eminar.*".r
  val LabPattern = ".*[L|l]aboratory.*".r

  def getRoomType(roomTypeString: String): RoomType = {
    roomTypeString match {
      case LecturePattern() => new RoomType(0, roomTypeString, "")
      case SeminarPattern() => new RoomType(1, roomTypeString, "")
      case LabPattern() => new RoomType(2, roomTypeString, "")
      case _ => new RoomType(-1, roomTypeString, "unknown room type")
    }
  }
}


class Room(
            var roomId: String,
            var roomType: RoomType,
            var roomCapacity: Int,
            var disabledAccess: Boolean,
            var partitions: ListBuffer[RoomPartition]
          )