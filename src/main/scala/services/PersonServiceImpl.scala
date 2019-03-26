package services

import dao.PersonDAO
import entities.people.Person
import javax.inject.{Inject, Singleton}

import scala.concurrent.Future

@Singleton
class PersonServiceImpl @Inject()(personDAO: PersonDAO)extends PersonService {
	override def addPerson(person: Person): Future[String] = personDAO.add(person)

	override def getPerson(id: Long): Future[Option[Person]] = personDAO.get(id)

	override def deletePerson(id: Long): Future[Int] = personDAO.delete(id)

	override def listAllPeople: Future[Seq[Person]] = personDAO.listAll
}
