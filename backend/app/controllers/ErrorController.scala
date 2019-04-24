package controllers

import javax.inject.Inject
import play.api.mvc.{AbstractController, ControllerComponents}
import views.ErrorPage

class ErrorController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def badRequest(string: String) = Action {
    BadRequest(ErrorPage.badRequest(string)).as("text/html")
  }

  def notFound(file: String) = Action {
    NotFound(ErrorPage.notFound(file)).as("text/html")
  }
}