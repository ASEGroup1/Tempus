package controllers

import java.io.FileNotFoundException

import javax.inject.Inject
import play.api.mvc._
import views.ErrorPage

import scala.io.Source

class HtmlController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def display(path: String, file: String) = Action {
    try {
      Ok(readFile(path, file)).as("text/html")
    } catch {
      case _: FileNotFoundException => NotFound(ErrorPage.notFound(file))
      case e: Exception => BadRequest(ErrorPage.badRequest(e))
    }
  }

  def display(path: String, fileName: String, ext: String): Action[AnyContent] =
    display(path, fileName + "." + ext)


  private def readFile(path: String, file: String): String = {
    Source.fromFile(path + "/" + file).mkString
  }
}