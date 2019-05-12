package controllers.crud

import db.{ModuleDao, StudentDao}
import entities.people.{Person, Student}
import javax.inject.{Inject, Singleton}
import org.json4s.DefaultFormats
import org.json4s.native.Serialization.write
import play.api.libs.json.JsObject
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class StudentController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  implicit val formats: DefaultFormats = DefaultFormats
  val studentFields = classOf[Student].getDeclaredFields.map(_.getName) ++ classOf[Person].getDeclaredFields.map(_.getName)

  def create = Action(parse.json) { request =>
    try {
      val body = request.body.asInstanceOf[JsObject].value
      StudentDao.insert(Student(studentFields.map(f => f -> (if (body.contains(f)) body(f) else null)).toMap))

      Ok(s"Inserted Student with id ${body("studentId")}")
    } catch {
      case e: Exception => BadRequest(e.getMessage)
    }
  }

  def read(id: Int) = Action {
    val student = StudentDao.get(id)
    if (student != null) Ok(write(student))
    else BadRequest(s"""{"message":"Could not find student with id $id"}""")
  }

  def update(id: Int) = Action(parse.json) { request =>
    try {
      val body = request.body.asInstanceOf[JsObject].value
      if (!StudentDao.delete(id)) BadRequest(s"""{"message":"Could not find student with id $id"}""")
      else {
        StudentDao.insert(Student(studentFields.map(f => f -> (if (body.contains(f)) body(f) else null)).toMap))
        Ok(s"Updated Student with id ${body("studentId")}")
      }
    } catch {
      case e: Exception => BadRequest(e.getMessage)
    }
  }

  def delete(id: Int) = Action {
    if (StudentDao.delete(id)) Ok(s"Removed student with id $id")
    else BadRequest(s"Could not remove student with id $id, ensure student exists")
  }

  def map(studentId: Int, moduleId: Int) = Action {
    try {
      //If either don't exist will through exception
      StudentDao.get(studentId)
      ModuleDao.get(moduleId)
      StudentDao.map(studentId, moduleId)
      Ok(s"Successfully mapped student with id $studentId to $moduleId")
    } catch {
      case e: Exception => BadRequest(e.getMessage)
    }
  }
}
