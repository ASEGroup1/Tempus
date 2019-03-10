package services.generator.locationgenerator

import entities.locations.Building
import services.generator.Generator

object BuildingGenerator extends Generator[Building] {
  override def gen = new Building(genInt, genStr, null)
}