package services

import com.google.inject.ImplementedBy
import entities.people.Person
import scala.concurrent.Future

@ImplementedBy(classOf[PersonServiceImpl])
trait PersonService {
	def addPerson(user:Person) : Future[String]
	def getPerson(id : Long) : Future[Option[Person]]
	def deletePerson(id : Long) : Future[Int]
	def listAllPeople : Future[Seq[Person]]
}
