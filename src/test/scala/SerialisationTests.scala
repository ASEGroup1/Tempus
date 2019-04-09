import java.io.{FileInputStream, FileOutputStream, ObjectInputStream, ObjectOutputStream}

import junit.framework.TestCase
import services.parser.TimeTableParser
import services.scheduler.Scheduler
import services.scheduler.poso.ScheduledClass
import services.sussexroomscraper.SussexRoomScraper

class SerialisationTests extends TestCase {
  val rooms = SussexRoomScraper.roomDataForSession
  val events = TimeTableParser.modules.flatMap(m => m._2.requiredSessions.map(m._1 -> _.durationInHours)).toSet

  def test() = {
    val sc = Scheduler.binPackSchedule(5, rooms, events).get.head

    val oos = new ObjectOutputStream(new FileOutputStream("/tmp/nflx"))
    oos.writeObject(sc)
    oos.close

    // (3) read the object back in
    val ois = new ObjectInputStream(new FileInputStream("/tmp/nflx"))
    val sClass = ois.readObject.asInstanceOf[ScheduledClass]
    ois.close

    // (4) print the object that was read back in
    println(sClass)
  }
}
