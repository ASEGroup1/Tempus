package services.generator.locationgenerator

import entities.locations.BuildingFloor
import services.generator.Generator

object BuildingFloorGenerator extends Generator[BuildingFloor] {
  override def gen() = new BuildingFloor(genInt, genInt(0, 5), null)
}
