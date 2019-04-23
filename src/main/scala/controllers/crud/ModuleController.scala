package controllers.crud

import db.{ModuleDao, StudentDao}
import entities.module.Module
import javax.inject.{Inject, Singleton}
import org.json4s.DefaultFormats
import org.json4s.native.Serialization.write
import play.api.libs.json.JsObject
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class ModuleController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  implicit val formats: DefaultFormats = DefaultFormats
  val moduleFields = classOf[Module].getDeclaredFields.map(_.getName)

  def create = Action(parse.json) { request =>
    try {
      val body = request.body.asInstanceOf[JsObject].value
      ModuleDao.insert(Module(moduleFields.map(f => f -> (if (body.contains(f)) body(f) else null)).toMap))

      Ok(s"Inserted Module with id ${body("moduleId")}")
    } catch {
      case e: Exception => BadRequest(e.getMessage)
    }
  }

  def read(id: Int) = Action {
    val module = ModuleDao.get(id)
    if (module != null) Ok(write(module))
    else BadRequest(s"""{"message":"Could not find module with id $id"}""")
  }

  def update(id: Int) = Action(parse.json) { request =>
    try {
      val body = request.body.asInstanceOf[JsObject].value
      if(!ModuleDao.delete(id)) BadRequest(s"""{"message":"Could not find student with id $id"}""")
      else {
        ModuleDao.insert(Module(moduleFields.map(f => f -> (if (body.contains(f)) body(f) else null)).toMap))
        Ok(s"Updated Student with id ${body("moduleId")}")
      }
    } catch {
      case e: Exception => BadRequest(e.getMessage)
    }
  }

  def delete(id: Int) = Action {
    if (ModuleDao.delete(id)) Ok(s"Removed module with id $id")
    else BadRequest(s"Could not remove module with id $id, ensure module exists")
  }

  def getModuleWithStudent(id: Int) = Action {
    try Ok(write(ModuleDao.get(id) -> StudentDao.getStudentsInModule(id)))
    catch {
      case e: Exception => BadRequest(e.getMessage)
    }
  }
}
