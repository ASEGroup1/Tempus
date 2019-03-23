package dao

import entities.people.Person
import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.concurrent.Execution.Implicits._
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._
import slick.lifted.{TableQuery, Tag}

import scala.collection.mutable.ListBuffer
import scala.concurrent.Future

@Singleton
class PersonDAOImpl @Inject()(dbConfigProvider: DatabaseConfigProvider) extends PersonDAO {

	private val dbConfig = dbConfigProvider.get[JdbcProfile]

	override def add(person: Person): Future[String] = {
		val db = Database.forConfig("mydb")
		try {
			db.run(people += person).map(res => "Person successfully added").recover {
				case ex: Exception => ex.getCause.getMessage
			}
		} finally
			db.close()
	}

	implicit val people = TableQuery[PersonTable]

	override def get(id: Long): Future[Option[Person]] = {
		val db = Database.forConfig("mydb")
		try {
			db.run(people.filter(_.personId === id).result.headOption)
		} finally
			db.close()
	}

	override def delete(id: Long): Future[Int] = {
		val db = Database.forConfig("mydb")
		try {
			db.run(people.filter(_.personId === id).delete)
		} finally
			db.close()
	}

	override def listAll: Future[Seq[Person]] = {
		val db = Database.forConfig("mydb")
		val seq = new ListBuffer[Person]()
		try {
			db.run(people.result)
		} finally
			db.close()
	}


	class PersonTable(tag: Tag) extends Table[Person](tag, "Person") {

		override def * =
			(personId, firstName, lastName, otherNames) <> (Person.tupled, Person.unapply)

		def personId = column[Long]("PersonID", O.PrimaryKey, O.AutoInc)

		def firstName = column[String]("FirstName")

		def lastName = column[String]("LastName")

		def otherNames = column[String]("OtherNames")
	}

}
