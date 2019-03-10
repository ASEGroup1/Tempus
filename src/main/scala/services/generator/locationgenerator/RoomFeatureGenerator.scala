package services.generator.locationgenerator

import entities.locations.RoomFeature
import services.generator.Generator

object RoomFeatureGenerator extends Generator[RoomFeature] {
  override def gen() = new RoomFeature(genInt, genStr, genStr)
}
