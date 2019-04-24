import junit.framework.TestCase
import org.junit.Assert.assertEquals
import services.sussexroomscraper.SussexRoomScraper

class RoomScraperTest extends TestCase {
  //TODO mock request
  def testIfScraperFetchesResults: Unit = {
    val rooms = SussexRoomScraper.roomDataForSession
    print(rooms)

    assertEquals(139, rooms.size)
  }
}
