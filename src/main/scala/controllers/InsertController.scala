package controllers

import db.Dao
import entities.module.Module
import entities.people.Student
import entities.people.Person
import javax.inject.{Inject, Singleton}
import play.api.libs.json.JsObject
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class InsertController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  val moduleFields = classOf[Module].getDeclaredFields.map(_.getName)


  def insertModule = Action(parse.json) { request =>
    try {
      val body = request.body.asInstanceOf[JsObject].value
      Dao.insert(Module(moduleFields.map(f => f -> (if (body.contains(f)) body(f) else null)).toMap))

      Ok(s"Inserted Module with id ${body("moduleId")}")
    } catch {
      case e:Exception => BadRequest(e.getMessage)
    }
  }
}