package entities.locations

import entities.Generator

object RoomFeature extends Generator[RoomFeature] {
	override def gen() = new RoomFeature(genInt, genStr, genStr)
}

class RoomFeature(
	var roomFeatureId: Int,
	var roomFeatureName: String,
	var roomFeatureDescription: String
)
