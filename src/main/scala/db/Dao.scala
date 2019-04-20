package db

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}
import java.sql.DriverManager
import java.sql.{ResultSet, SQLException}

import entities.module.Module
import entities.people.Student
import org.apache.commons.dbutils.ResultSetHandler
import org.apache.commons.dbutils.QueryRunner
import services.scheduler.poso.ScheduledClass

import scala.util.Random

object Dao {
  val run = new QueryRunner()

  val timeTableHandler = new ResultSetHandler[Array[AnyRef]]() {
    @throws[SQLException]
    override def handle(rs: ResultSet): Array[AnyRef] = if (rs.next) Array(rs.getObject(2)) else null
  }

  val studentHandler = new ResultSetHandler[Array[AnyRef]]() {
    @throws[SQLException]
    override def handle(rs: ResultSet): Array[AnyRef] = if(rs.next) Array(Student(rs)) else null
  }

  //Even though these are set in environment variables there is currently a bug in intellij that sets env variables as properties instead in play
  private[this] val conn =  DriverManager.getConnection(sys.props("DB_URL"), sys.props("DB_USER"), sys.props("DB_PASSWORD"))

  def removeStudent(id: Int) =
    //Update method returns number of rows updated
    run.update(conn, s"DELETE FROM STUDENT WHERE ID = $id") == 1

  def getStudent(id: Int) = run.query(conn, s"SELECT * FROM STUDENT WHERE ID = $id", studentHandler).head

  def insertTimeTable(timeTable: List[ScheduledClass]): Unit = run.update(conn, s"INSERT INTO TIMETABLES VALUES(${Random.nextInt},?)", serialize(timeTable))

  def insert(student: Student) =
    run.update(conn, s"INSERT INTO STUDENT VALUES(${student.studentId}, NULL, ${student.currentFehqLevelCompleted}, NULL, " +
                                                               s"'${student.firstName}', '${student.lastName}', '${student.otherNames}')")
  def insert(module: Module) =
    run.update(conn, s"INSERT INTO MODULE VALUES(${module.moduleId}, '${module.moduleName}', '${module.moduleDescription}', NULL, NULL)")

  def retrieveTimeTable(id:Integer) =
    deserialize(run.query(conn, "SELECT * FROM TIMETABLES WHERE ID=?", timeTableHandler, id).head.asInstanceOf[Array[Byte]])

  private[this] def serialize(obj: Any) = {
    val bo = new ByteArrayOutputStream
    new ObjectOutputStream(bo).writeObject(obj)

    bo.toByteArray
  }

  private[this] def deserialize(binary: Array[Byte]) =
    new ObjectInputStream(new ByteArrayInputStream(binary)).readObject.asInstanceOf[List[ScheduledClass]]
}
