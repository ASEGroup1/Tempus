package db

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}
import java.sql.ResultSet

import services.scheduler.poso.ScheduledClass

import scala.util.Random

object TimeTableDao extends Dao[List[ScheduledClass]] {
  override val tableName = "TIMETABLE"
  override val handler = (rs: ResultSet) => if (rs.next) Array(rs.getObject(2)) else Array()

  override def insert(timeTable: List[ScheduledClass]) = run.update(conn, s"INSERT INTO TIMETABLES VALUES(${Random.nextInt},?)", serialize(timeTable))

  override def get(id: Int) =
    deserialize(run.query(conn, "SELECT * FROM TIMETABLES WHERE ID=$id", handler).head.asInstanceOf[Array[Byte]])

  private[this] def serialize(obj: Any) = {
    val bo = new ByteArrayOutputStream
    new ObjectOutputStream(bo).writeObject(obj)

    bo.toByteArray
  }

  private[this] def deserialize(binary: Array[Byte]) =
    new ObjectInputStream(new ByteArrayInputStream(binary)).readObject.asInstanceOf[List[ScheduledClass]]
}
