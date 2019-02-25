package controllers

import javax.inject.Inject
import play.api.mvc._

import scala.io.Source

class HtmlController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def display(path: String, file: String) = Action {
    Ok(Source.fromFile(path + "/" + file).mkString).as("text/html")
  }
}