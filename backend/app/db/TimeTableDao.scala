package db

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}
import java.sql.ResultSet

import services.scheduler.poso.ScheduledClass

object TimeTableDao extends Dao[List[ScheduledClass]] {
  override val tableName = "TIMETABLE"
  override val handler = (rs: ResultSet) => if (rs.next) Array(rs.getObject(2)) else Array()

  def insert(timeTable: List[ScheduledClass], name: String) =
    run.update(conn, s"INSERT INTO TIMETABLES VALUES('$name',?)", serialize(timeTable))

  def get(name: String) =
    deserialize(run.query(conn, s"SELECT * FROM TIMETABLES WHERE NAME='$name'", handler).head.asInstanceOf[Array[Byte]])

  private[this] def serialize(obj: List[ScheduledClass]) = {
    val bo = new ByteArrayOutputStream
    new ObjectOutputStream(bo).writeObject(obj)
    bo.toByteArray
  }

  private[this] def deserialize(binary: Array[Byte]) = {
    new ObjectInputStream(new ByteArrayInputStream(binary)).readObject.asInstanceOf[List[ScheduledClass]]
  }

  override def insert(data: List[ScheduledClass]) = ???

  def getTimetableNames() =
    run.query(conn, "SELECT NAME FROM TIMETABLES", (rs: ResultSet) => if (rs.next) Array(rs.getString(1)) else Array())

}
