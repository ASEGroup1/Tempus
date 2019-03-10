package services.generator.locationgenerator

import java.util.UUID

import services.generator.Generator

object RoomGenerator extends Generator[String] {
  override def gen(): String = UUID.randomUUID.toString

  def get(roomCount: Int) = {
    if(roomCount > 137) println(s"$roomCount is greater than 137 (total room number), only 137 rooms have been returned.")
    SussexRoomScraper.roomDataForSession.take(roomCount)
  }
}
