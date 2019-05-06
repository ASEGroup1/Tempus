package db

import java.sql.ResultSet

import entities.people.Student

object StudentDao extends Dao[Student] {
  override val tableName = "STUDENT"
  override val handler = (rs: ResultSet) => if (rs.next) Array(Student(rs)) else Array()

  override def insert(student: Student) =
      run.update(conn, s"INSERT INTO STUDENT VALUES(${student.studentId}, NULL, ${student.currentFehqLevelCompleted}, NULL, " +
        s"'${student.firstName}', '${student.lastName}', '${student.otherNames}')")
}
