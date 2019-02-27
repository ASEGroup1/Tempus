import junit.framework.TestCase
import org.junit.Assert.assertEquals
import services.sussexroomscraper.SussexRoomScraper

class RoomScraperTest extends TestCase {
  //TODO mock request
  def testIfScraperFetchesResults: Unit = {
    val rooms = SussexRoomScraper.scrape
    print(rooms)

    assertEquals(122, rooms.size)
  }
}
