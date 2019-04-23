package db

import java.sql.ResultSet

import entities.people.Student

import scala.collection.mutable

object StudentDao extends Dao[Student] {
  def map(studentId: Int, moduleId: Int) =
    run.update(conn, s"INSERT INTO STUDENT_MODULE VALUES($studentId, $moduleId)")

  override val tableName = "STUDENT"
  override val handler = (rs: ResultSet) => {
    var students = mutable.ArrayBuffer[Student]()
    while(rs.next) students += Student(rs)
    students.toArray
  }

  override def insert(student: Student) =
      run.update(conn, s"INSERT INTO STUDENT VALUES(${student.studentId}, NULL, ${student.currentFehqLevelCompleted}, NULL, " +
        s"'${student.firstName}', '${student.lastName}', '${student.otherNames}')")

  def getStudentsInModule(moduleId: Int) =
    run.query(conn, s"SELECT * FROM STUDENT WHERE ID IN" +
      s" (SELECT STUDENT_ID FROM STUDENT_MODULE WHERE MODULE_ID = $moduleId)", handler)
}
