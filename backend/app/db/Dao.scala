package db

import java.sql.DriverManager

import org.apache.commons.dbutils.{QueryRunner, ResultSetHandler}

trait Dao[T] {
  val tableName: String
  val handler: ResultSetHandler[Array[AnyRef]]
  //Even though these are set in environment variables there is currently a bug in intellij that sets env variables as properties instead in play
  val conn = DriverManager.getConnection(sys.props("DB_URL"), sys.props("DB_USER"), sys.props("DB_PASSWORD"))
  val run = new QueryRunner()

  def insert(data: T)

  def delete(id: Int) =
  //Update method returns number of rows updated
    run.update(conn, s"DELETE FROM $tableName WHERE ID = $id") == 1

  def get(id: Int) = run.query(conn, s"SELECT * FROM $tableName WHERE ID = $id", handler).head
}
