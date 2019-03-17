package services.parser

import java.util.regex.Pattern

import entities.School
import entities.module.{Module, RequiredSession}
import services.Utils

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.io.Source

object TimeTableParser {
  val TimeTablePattern = Pattern.compile("\\\"?[0-9]L[\\s]([\\S\\s]*?)\\([\\S\\s]+?\\)\\/[0-9]+\\\"?,([A-Za-z]+),([0-9]{1,2}:[0-9]{1,2}),([0-9]{1,2}:[0-9]{1,2}),([\\S\\s]*?),[\\S\\s]*?,([\\S\\s]*?),([\\S\\s]*?),")
  val SchoolPattern = Pattern.compile("[A-Z][0-9]{4},([\\S\\s]*?),")

  val timeTableCsvStr = Source.fromFile(getClass.getResource("/input/Gregory_Mitten_Full_Timetable.csv").getPath).mkString

  val schools: Map[String, School] = {
    val schoolMatcher = SchoolPattern.matcher(timeTableCsvStr)
    var schools = mutable.Set[School]()
    var schoolId = 0

    while (schoolMatcher.find) {
      schools += new School(schoolId, Utils.toSnake(schoolMatcher.group(1)), null)
      schoolId += 1
    }

    schools.groupBy(_.schoolName).map(_._2.head).filter(!_.schoolName.contains("(")).map(s => s.schoolName -> s).toMap
  }

  val modules: Map[String, Module] = {
    var modules = mutable.Set[Module]()
    val moduleMatcher = TimeTablePattern.matcher(timeTableCsvStr)
    var currentModule = new Module()
    var sessions = mutable.Set[RequiredSession]()

    while (moduleMatcher.find) {
      sessions += new RequiredSession(0, moduleMatcher.group(4).split(":")(0).toInt - moduleMatcher.group(3).split(":")(0).toInt)
      if (Utils.toSnake(moduleMatcher.group(1)) != currentModule.moduleName) {
        if (!currentModule.moduleName.isEmpty) {
          currentModule.requiredSessions = sessions
          modules += currentModule
          sessions = mutable.Set[RequiredSession]()
        }
        currentModule = new Module(0, moduleMatcher.group(6), Utils.toSnake(moduleMatcher.group(1)), "",
          if (schools.contains(Utils.toSnake(moduleMatcher.group(7)))) schools(Utils.toSnake(moduleMatcher.group(7))) else null,
          ListBuffer(2),
          null)
      }
    }
    modules.map(m => m.moduleName -> m).toMap
  }
}
