package entities.locations

import entities.Generator

import scala.collection.mutable.ListBuffer

object RoomPartition extends Generator[RoomPartition] {
	override def gen() = new RoomPartition(genInt, genStr, genInt(1, 200), null, null)
}

class RoomPartition(
	var roomPartitionId: Int,
	var name: String,
	var capacity: Int,
	var roomPartitionType: RoomType,
	var features: ListBuffer[RoomFeature]
)
