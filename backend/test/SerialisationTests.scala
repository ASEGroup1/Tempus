import java.io.{ByteArrayInputStream, ByteArrayOutputStream, FileInputStream, FileOutputStream, ObjectInputStream, ObjectOutputStream}

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream
import db.Dao
import junit.framework.TestCase
import org.junit.Assert.assertEquals
import services.parser.TimeTableParser
import services.scheduler.Scheduler
import services.scheduler.poso.ScheduledClass
import services.sussexroomscraper.SussexRoomScraper

class SerialisationTests extends TestCase {
  val rooms = SussexRoomScraper.roomDataForSession
  val events = TimeTableParser.modules
  val dao = new Dao

  def testIfSerialisedItemsAreTheSameDeserialised() = {
    val classes = Scheduler.binPackSchedule(5, rooms, events).get

    val bo = new ByteArrayOutputStream
    new ObjectOutputStream(bo).writeObject(classes)
    val serializedObject = bo

    assertEquals(classes.size, new ObjectInputStream(new ByteArrayInputStream(serializedObject.toByteArray)).readObject.asInstanceOf[List[ScheduledClass]].size)
  }

  //TODO remove after we decided when we're storing data
  def testIfSerializedTimeTableIsInserted =  dao.insertTimeTable(Scheduler.binPackSchedule(5, rooms, events).get)

  //TODO remove after we decided when we're storing data
  def testIfRetrievesDeserializedTimeTable = assert(dao.retrieveTimeTable(0).nonEmpty)
}
