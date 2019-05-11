package db

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}
import java.sql.ResultSet

import services.scheduler.poso.ScheduledClass

import scala.collection.mutable

object TimeTableDao extends Dao[List[ScheduledClass]] {
  override val tableName = "TIMETABLE"
  override val handler = (rs: ResultSet) => if (rs.next) Array(rs.getObject(2)) else Array()

  def insert(timeTable: Map[String, Iterable[List[String]]], name: String) =
    run.update(conn, s"INSERT INTO TIMETABLES VALUES('$name',?)", serialize(timeTable))

  def get(name: String) =
    deserialize(run.query(conn, s"SELECT * FROM TIMETABLES WHERE NAME='$name'", handler).head.asInstanceOf[Array[Byte]])

  private[this] def serialize(obj: Map[String, Iterable[List[String]]]) = {
    val bo = new ByteArrayOutputStream
    new ObjectOutputStream(bo).writeObject(obj)
    bo.toByteArray
  }

  private[this] def deserialize(binary: Array[Byte]) = {
    new ObjectInputStream(new ByteArrayInputStream(binary)).readObject
  }

  override def insert(data: List[ScheduledClass]) = ???

  def getTimetableNames() =
    run.query(conn, "SELECT NAME FROM TIMETABLES", (rs: ResultSet) => {
      var results = mutable.ListBuffer[String]()
      while(rs.next) results += rs.getString(1)

      results
    })
}
