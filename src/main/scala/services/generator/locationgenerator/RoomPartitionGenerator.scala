package services.generator.locationgenerator

import entities.locations.RoomPartition
import services.generator.Generator

object RoomPartitionGenerator extends Generator[RoomPartition] {
  override def gen() = new RoomPartition(genInt, genStr, genInt(1, 200), null, null)
}
