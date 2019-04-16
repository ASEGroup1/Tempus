package controllers

import javax.inject.{Inject, Singleton}
import play.api.libs.json._
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class InsertController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
    * Create an Action to render an HTML page with a welcome message.
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def sayHello = Action(parse.json) { request =>
    request.body.validate[(String, Long)].map{
      case (name, age) => Ok("Hello " + name + ", you're "+age)
    }.recoverTotal{
      e => BadRequest("Detected error:"+ JsError.toJson(e))
    }
  }
}