package controllers

import entities.people.Person
import javax.inject._
import play.api.mvc._
import services.PersonService
import services.generator.studentgenerator.StudentGenerator

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents, personService: PersonService) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
	  personService.addPerson(Person(1, "James", "Fernando", ""))
    Ok("<html><head><title>Student Generator</title></head><body>" + StudentGenerator.generate(100).toString +"</body></html>").as("text/html")
  }
}
