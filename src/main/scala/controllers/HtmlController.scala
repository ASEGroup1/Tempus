package controllers

import java.io.FileNotFoundException

import javax.inject.Inject
import play.api.mvc._
import views.ErrorPage

import scala.io.Source

class HtmlController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def display(path: String, file: String, contentType:String) = Action {
    try {
      if(contentType.isEmpty)
        Ok(readFile(path, file))
      else
        Ok(readFile(path, file)).as(contentType)
    } catch {
      case _: FileNotFoundException => NotFound(ErrorPage.notFound(file)).as("text/html")
      case e: Exception => BadRequest(ErrorPage.badRequest(e)).as("text/html")
    }
  }

  def displayFile(path: String, fileName: String, ext: String, contentType: String): Action[AnyContent] =
    display(path, fileName + "." + ext, contentType)


  private def readFile(path: String, file: String): String = {
    Source.fromFile(path + "/" + file).mkString
  }
}