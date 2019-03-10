package services.generator.roomgenerator

import java.util.UUID

import services.generator.Generator
import services.sussexroomscraper.SussexRoomScraper

object RoomGenerator extends Generator[String] {
  override def gen(): String = UUID.randomUUID.toString

  def get(roomCount: Int) = {
    if(roomCount > 137) println(s"$roomCount is greater than 137 (total room number), only 137 rooms have been returned.")
    SussexRoomScraper.roomDataForSession.take(roomCount)
  }
}