package entities.people

import java.util.regex.Pattern

import entities.course.Course
import entities.module.Module
import services.Utils
import services.generator.Generator
import services.parser.TimeTableParser
import services.parser.TimeTableParser.getClass

import scala.collection.mutable
import scala.io.Source


object Student extends Generator[Student] {
  val modules:Map[String, Module] = TimeTableParser.modules.map(m => Utils.reduceSpecificity(m.moduleName) -> m).toMap
  val moduleChoicesStr = Source.fromFile(getClass.getResource("/input/Pathways.csv").getPath).mkString
  val PathwayPattern = Pattern.compile(".*?,([A-Z][0-9]+)(.*)")

  val moduleChoices: Map[String, mutable.Set[Module]] = {
    class ModuleChoice(val pathwayCode: String, val moduleName: String)

    val moduleChoicesMatcher = PathwayPattern.matcher(moduleChoicesStr)
    var moduleChoices = mutable.Set[ModuleChoice]()

    while (moduleChoicesMatcher.find()) moduleChoices += new ModuleChoice(moduleChoicesMatcher.group(1), Utils.reduceSpecificity(moduleChoicesMatcher.group(2)))

    val x = moduleChoices.groupBy(_.pathwayCode).map(mc => mc._1 -> mc._2.map(m => if(modules.contains(m.moduleName)) modules(m.moduleName) else new Module(m.moduleName)))

    print()

    null
  }

  override def generate(): Student = {
    null
  }
}
class Student extends Person {
  var studentId: Int = _
  var course: Course = _
  var currentFehqLevelCompleted: Int = _
  var academicAdvisor: Staff = _
}
