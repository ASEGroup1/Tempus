package dao

import entities.people.Person
import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._
import slick.lifted.{TableQuery, Tag}

import scala.collection.mutable.ListBuffer
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PersonDAOImpl @Inject()(dbConfigProvider: DatabaseConfigProvider) extends PersonDAO {

	private val dbConfig = dbConfigProvider.get[JdbcProfile]
	private val db = dbConfig.db

	override def add(person: Person): Future[String] = try {
		val executionContext = ExecutionContext.global
		db.run(people += person)
			.map(res => "Person successfully added")(executionContext).recover {
			case ex: Exception => ex.getCause.getMessage
		}(executionContext)
	}
	finally
		db.close()

	implicit val people = TableQuery[PersonTable]

	override def get(id: Long): Future[Option[Person]] = {
		try {
			db.run(people.filter(_.personId === id).result.headOption)
		} finally
			db.close()
	}

	override def delete(id: Long): Future[Int] = {
		try {
			db.run(people.filter(_.personId === id).delete)
		} finally
			db.close()
	}

	override def listAll: Future[Seq[Person]] = {
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
