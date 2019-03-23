package dao

import com.google.inject.ImplementedBy
import entities.people.Person
import scala.concurrent.Future

@ImplementedBy(classOf[PersonDAOImpl])
trait PersonDAO {
	def add(user:Person) : Future[String]
	def get(id : Long) : Future[Option[Person]]
	def delete(id : Long) : Future[Int]
	def listAll : Future[Seq[Person]]
}
