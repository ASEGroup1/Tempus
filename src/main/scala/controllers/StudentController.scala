package controllers

import db.Dao
import entities.people.{Person, Student}
import javax.inject.{Inject, Singleton}
import play.api.libs.json.JsObject
import play.api.mvc.{AbstractController, ControllerComponents}
import org.json4s.DefaultFormats
import org.json4s.native.Serialization.write

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

  def delete(id: Int) = Action {
    if (Dao.removeStudent(id)) Ok(s"Removed student with id $id")
    else BadRequest(s"Could not remove student with id $id, ensure student exists")
  }
}
