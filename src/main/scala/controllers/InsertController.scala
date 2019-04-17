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
  val studentFields = classOf[Student].getDeclaredFields.map(_.getName) ++ classOf[Person].getDeclaredFields.map(_.getName)
  val moduleFields = classOf[Module].getDeclaredFields.map(_.getName)

  def insertStudent = Action(parse.json) { request =>
    try {
      val body = request.body.asInstanceOf[JsObject].value
      Dao.insert(Student(studentFields.map(f => f -> (if (body.contains(f)) body(f) else null)).toMap))

      Ok(s"Inserted Student with id {${body("studentId")}")
    } catch {
      case e:Exception => BadRequest(e.getMessage)
    }
  }

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