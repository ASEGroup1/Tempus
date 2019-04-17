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

  def insertStudent = Action(parse.json) { request =>
    val body = request.body.asInstanceOf[JsObject].value
    Dao.insert(Student(
      (classOf[Student].getDeclaredFields.map(_.getName) ++ classOf[Person].getDeclaredFields.map(_.getName))
        .map(f => f -> (if (body.contains(f)) body(f) else null)).toMap))

    Ok("Inserted Student")
  }

  def insertModule = Action(parse.json) { request =>
    val body = request.body.asInstanceOf[JsObject].value
    Dao.insert(Module(classOf[Module].getDeclaredFields.map(_.getName)
        .map(f => f -> (if (body.contains(f)) body(f) else null)).toMap))

    Ok("Inserted Module")
  }
}