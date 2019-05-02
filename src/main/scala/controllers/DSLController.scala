package controllers

import javax.inject.{Inject, Singleton}
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import services.parser.dsl.{DSLCompiler, FilterList}

@Singleton
class DSLController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def setDSL() = Action {implicit request =>
    try{
      FilterList.setFilters(Form("dsl" -> text).bindFromRequest.get)
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
      val newFilters = FilterList.addFilters(Form("dsl" -> text).bindFromRequest.get)
      Ok("Added filters: \""+newFilters.mkString("\", \"")+"\"\nCurrent Filters: " + getDSLText)
    } catch{
      case e:Exception =>
        BadRequest(e.getMessage)
    }
  }

  def removeDSL() = Action {implicit request =>
    try{
      val filtersToRemove = Form("dsl" -> text).bindFromRequest.get.split("\\s*;\\s*")
      FilterList.removeFilters(filtersToRemove)
      Ok("Removed filters: \""+filtersToRemove.mkString("\", \"")+"\"\nCurrent Filters: " + getDSLText)
    } catch{
      case e:Exception =>
        BadRequest(e.getMessage)
    }
  }

  def getCurrentDSL() = Action {
    println(getDSLNamesJSON)
    Ok(Json.parse(getDSLNamesJSON)).as("application/json")
  }

  def getDSLReferenceObjects() = Action {
    Ok (Json.parse(DSLCompiler.referenceTable.values.map(_.toJson).mkString("[", ",", "]"))).as("application/json")
  }



  private def getDSLNamesJSON: String = {
    val info = FilterList.getFilterInfo

    val map = info.map(f => {
      "\"" + f._1 + "\": \"" + f._2 + "\""
    })

    val mappedString = map.mkString(", ")

    val res = "{" + mappedString.replaceAll("\"", "\\\"").replaceAll("\r\n", "\\\\n") + "}"
    res
  }

  private def getDSLText = "\""+FilterList.getFilterNames.mkString("\", \"")+"\""


}
