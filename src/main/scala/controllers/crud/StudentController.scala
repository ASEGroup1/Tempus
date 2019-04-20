package controllers.crud

import db.Dao
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
      Dao.insert(Student(studentFields.map(f => f -> (if (body.contains(f)) body(f) else null)).toMap))

      Ok(s"Inserted Student with id ${body("studentId")}")
    } catch {
      case e: Exception => BadRequest(e.getMessage)
    }
  }

  def read(id: Int) = Action {
    val student = Dao.getStudent(id)
    if (student != null) Ok(write(student))
    else BadRequest(s"""{"message":"Could not find student with id $id"}""")
  }

  def update(id: Int) = Action(parse.json) { request =>
    try {
      val body = request.body.asInstanceOf[JsObject].value
      if(!Dao.delete(id, "MODULE")) BadRequest(s"""{"message":"Could not find student with id $id"}""")
      else {
        Dao.insert(Student(studentFields.map(f => f -> (if (body.contains(f)) body(f) else null)).toMap))
        Ok(s"Updated Student with id ${body("studentId")}")
      }
    } catch {
      case e: Exception => BadRequest(e.getMessage)
    }
  }

  def delete(id: Int) = Action {
    if (Dao.delete(id, "STUDENT")) Ok(s"Removed student with id $id")
    else BadRequest(s"Could not remove student with id $id, ensure student exists")
  }
}
