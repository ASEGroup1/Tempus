import java.io.{ByteArrayInputStream, ByteArrayOutputStream, FileInputStream, FileOutputStream, ObjectInputStream, ObjectOutputStream}

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream
import junit.framework.TestCase
import org.junit.Assert.assertEquals
import services.parser.TimeTableParser
import services.scheduler.Scheduler
import services.scheduler.poso.ScheduledClass
import services.sussexroomscraper.SussexRoomScraper

class SerialisationTests extends TestCase {
  val rooms = SussexRoomScraper.roomDataForSession
  val events = TimeTableParser.modules.flatMap(m => m._2.requiredSessions.map(m._1 -> _.durationInHours)).toSet

  def testIfSerialisedItemsAreTheSameDeserialised() = {
    val classes = Scheduler.binPackSchedule(5, rooms, events).get

    val bo = new ByteArrayOutputStream
    new ObjectOutputStream(bo).writeObject(classes)
    val serializedObject = bo

    assertEquals(classes.size, new ObjectInputStream(new ByteArrayInputStream(serializedObject.toByteArray)).readObject.asInstanceOf[List[ScheduledClass]].size)
  }
}
