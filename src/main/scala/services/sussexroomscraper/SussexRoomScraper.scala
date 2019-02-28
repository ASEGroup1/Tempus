package services.sussexroomscraper

import java.util.regex.Pattern

import com.mashape.unirest.http.Unirest
import services.scheduler.poso.Room

import scala.collection.mutable.ArrayBuffer

object SussexRoomScraper {
  val BuildingNamePattern = Pattern.compile("<h3>(.*)</h3><table.*?>[\\S\\s]*?</table>")

  val RowDataPattern = Pattern.compile("<tr><td>(.*)</td></td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>.*?</td><td>.*?</td><td>.*?</td><td>.*?</td>" +
    "<td>(.*?)</td><td>.*?</td><td>.*?</td><td>.*?</td><td>.*?</td><td>(.*?)</td><td>.*?</td><td>.*?</td>")

  val PageUrl = "http://www.sussex.ac.uk/studentsystems/roomfac.php"

  val roomDataForSession = {
    val matcher = RowDataPattern.matcher(Unirest.get(PageUrl).asString.getBody)

    var rooms = ArrayBuffer[Room]()
    while (matcher.find()) rooms += Room(matcher)
    rooms
  }
}