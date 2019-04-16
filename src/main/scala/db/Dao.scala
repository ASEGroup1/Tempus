package db

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}
import java.sql.DriverManager
import java.sql.{ResultSet, SQLException}

import org.apache.commons.dbutils.ResultSetHandler
import org.apache.commons.dbutils.QueryRunner
import services.scheduler.poso.ScheduledClass

import scala.util.Random

class Dao {
  val run = new QueryRunner()

  val h = new ResultSetHandler[Array[AnyRef]]() {
    @throws[SQLException]
    override def handle(rs: ResultSet): Array[AnyRef] = if (rs.next) Array(rs.getObject(2)) else null
  }

  private[this] val conn =  DriverManager.getConnection(sys.env("DB_URL"), sys.env("DB_USER"), sys.env("DB_PASSWORD"))

  def insertTimeTable(timeTable: List[ScheduledClass]): Unit = run.update(conn, s"INSERT INTO TIMETABLES VALUES(${Random.nextInt},?)", serialize(timeTable))

  def retrieveTimeTable(id:Integer) =
    deserialize(run.query(conn, "SELECT * FROM TIMETABLES WHERE ID=?", h, id).head.asInstanceOf[Array[Byte]])

  private[this] def serialize(obj: Any) = {
    val bo = new ByteArrayOutputStream
    new ObjectOutputStream(bo).writeObject(obj)

    bo.toByteArray
  }

  private[this] def deserialize(binary: Array[Byte]) =
    new ObjectInputStream(new ByteArrayInputStream(binary)).readObject.asInstanceOf[List[ScheduledClass]]

}
