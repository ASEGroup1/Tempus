package controllers

import javax.inject.{Inject, Singleton}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{AbstractController, ControllerComponents}
import services.parser.dsl.{DSLCompiler, FilterList}

@Singleton
class DSLController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def setDSL() = Action {implicit request =>
    try{
      FilterList.filters = DSLCompiler.compile(Form("dsl" -> text).bindFromRequest.get)
      Ok("New filters: " + getDSLText)
    } catch{
      case e:Exception =>
        BadRequest(e.getMessage)
    }
  }

  def getDSLForm(controller: String) = Action {
    Ok("<html>\n\t<body>\n\t\tDSL:<br>\n\t\t<textarea name=\"dsl\" form=\"dslform\" action=\"" + controller +"\"></textarea>\n\t\t<form id=\"dslform\" method=\"post\">\n\t\t\t<input type=\"submit\">\n\t\t</form>\n\t</body>\n</html>").as("text/html")
  }

  def addDSL() = Action {implicit request =>
    try{
      val newFilters = DSLCompiler.compile(Form("dsl" -> text).bindFromRequest.get)
      FilterList.filters ++= newFilters
      Ok("Added filters: \""+newFilters.keySet.mkString("\", \"")+"\"\nCurrent Filters: " + getDSLText)
    } catch{
      case e:Exception =>
        BadRequest(e.getMessage)
    }
  }

  def removeDSL() = Action {implicit request =>
    try{
      val filtersToRemove = Form("dsl" -> text).bindFromRequest.get.split("\\s*;\\s*")
      FilterList.filters --= filtersToRemove
      Ok("Removed filters: \""+filtersToRemove.mkString("\", \"")+"\"\nCurrent Filters: " + getDSLText)
    } catch{
      case e:Exception =>
        BadRequest(e.getMessage)
    }
  }

  def getCurrentDSL() = Action {
    Ok("Current filters: " + getDSLText)
  }

  private def getDSLText = "\""+FilterList.filters.keySet.mkString("\", \"")+"\""

}
