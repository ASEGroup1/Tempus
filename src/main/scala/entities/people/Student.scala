package entities.people

import java.util.regex.Pattern

import entities.course.{Course, CourseRole}
import entities.module.{Module, ModuleRole}
import exceptions.InvalidJsonException
import services.Utils
import services.generator.Generator
import services.parser.TimeTableParser
import services.JsonUtils._
import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.util.Random


object Student extends Generator[Student] {
  def apply(json:Map[String, Any]): Student = {
    if(json("studentId") == null) throw new InvalidJsonException("Missing Id")

    new Student(extractInt(json("studentId")), null, extractInt(json("currentFehqLevelCompleted")), null,
      extractInt(json("personId")), extractString(json("firstName")), extractString(json("lastName")), extractString(json("otherNames")), ListBuffer(), Set())
  }

  val studentRole = new ModuleRole(0, "Student", "")
  val modules: Map[String, Module] = TimeTableParser.modules
  val moduleChoicesStr = Source.fromFile(getClass.getResource("/input/Pathways.csv").getPath).mkString
  val PathwayPattern = Pattern.compile("([A-Z][0-9]{4}[A-Z]-.*?),[A-Z][0-9]+(.*)")

  val pathways: Map[String, mutable.Set[Module]] = {
    class ModuleChoice(val pathwayCode: String, val moduleName: String)

    val moduleChoicesMatcher = PathwayPattern.matcher(moduleChoicesStr)
    var moduleChoices = mutable.Set[ModuleChoice]()

    while (moduleChoicesMatcher.find()) moduleChoices += new ModuleChoice(moduleChoicesMatcher.group(1), Utils.toSnake(moduleChoicesMatcher.group(2)))

    moduleChoices.groupBy(_.pathwayCode).map(mc => mc._1 -> mc._2.map(m => if (modules.contains(m.moduleName)) modules(m.moduleName) else new Module(m.moduleName)))
  }

  override def generate(): Student = {
    new Student(0, null, 0, null, 0, "", "",
      "", null, pathways(pathways.keys.toSeq(Random.nextInt(pathways.size - 1))).map(p => (studentRole, p)).toSet)
  }
}

class Student(var studentId: Int, var course: Course, var currentFehqLevelCompleted: Int, var academicAdvisor: Staff,
              personId: Int, firstName: String, lastName: String, otherNames: String, courses: ListBuffer[(CourseRole, Course)], modules: Set[(ModuleRole, Module)]
             ) extends Person(personId, firstName, lastName, otherNames, courses, modules) {
}
