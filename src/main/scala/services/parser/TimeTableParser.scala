package services.parser

import java.util.regex.Pattern

import entities.School
import entities.course.Course
import entities.locations.Building
import entities.module._
import entities.people.Person
import services.Utils

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.io.Source

object TimeTableParser {
  val TimeTablePattern = Pattern.compile("\\\"?[0-9]L[\\s]([\\S\\s]*?)\\([\\S\\s]+?\\)\\/[0-9]+\\\"?,([A-Za-z]+),([0-9]{1,2}:[0-9]{1,2}),([0-9]{1,2}:[0-9]{1,2}),([\\S\\s]*?),([10]*?),([\\S\\s]*?),([\\S\\s]*?),")
  val SchoolPattern = Pattern.compile("[A-Z][0-9]{4},([\\S\\s]*?),")

  val timeTableCsvStr = Source.fromFile(getClass.getResource("/input/Gregory_Mitten_Full_Timetable.csv").getPath).mkString

  val schools: Map[String, School] = {
    val schoolMatcher = SchoolPattern.matcher(timeTableCsvStr)
    var schools = mutable.Set[String]()
    while (schoolMatcher.find) {
      schools += Utils.toSnake(schoolMatcher.group(1))
    }
    schools.filter(!_.contains("(")).zipWithIndex.map(s => s._1 -> new School(s._2, s._1, null)).toMap
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
        currentModule = new Module(0, moduleMatcher.group(7), Utils.toSnake(moduleMatcher.group(1)), "",
          if (schools.contains(Utils.toSnake(moduleMatcher.group(8)))) schools(Utils.toSnake(moduleMatcher.group(8))) else new School(0, "null", new Building(0, "null", ListBuffer())),
          ListBuffer(2),
          mutable.Set[RequiredSession]())
      }
    }
    modules.map(m => m.moduleName -> m).toMap
  }

  val mfheq: Set[ModuleFehqLevel] = {
    var modules = mutable.Set[ModuleFehqLevel]()
    val moduleMatcher = TimeTablePattern.matcher(timeTableCsvStr)

    var currentModule = new Module()
    var currentSessions = mutable.Set[RequiredSession]()
    var currentSessionStructure = ListBuffer[ModuleSessionStructure]()

    var moduleId = 0
    var sessionId = 0
    var mssId = 0

    while (moduleMatcher.find) {
      val ses = new RequiredSession({
        sessionId += 1; sessionId
      }, moduleMatcher.group(4).split(":")(0).toInt - moduleMatcher.group(3).split(":")(0).toInt)
      currentSessions += ses
      val nullInt = 0

      moduleMatcher.group(6).toCharArray.zipWithIndex.filter(_._1 == '1').map(_._2).foreach(e => currentSessionStructure += new ModuleSessionStructure({
        mssId += 1; mssId
      }, e, new ModuleSessionType(0, "Null", "Null"), 1, nullInt, ses))
      if (Utils.toSnake(moduleMatcher.group(1)) != currentModule.moduleName) {
        if (!currentModule.moduleName.isEmpty) {
          currentModule.requiredSessions = currentSessions
          modules += new ModuleFehqLevel(currentModule, nullInt, ListBuffer[ModuleFehqLevel](), ListBuffer[Course](), nullInt, ListBuffer[(ModuleRole, Person)](), currentSessionStructure)
          currentSessions = mutable.Set[RequiredSession]()
          currentSessionStructure = ListBuffer[ModuleSessionStructure]()
        }
        currentModule = new Module({
          moduleId += 1; moduleId
        }, moduleMatcher.group(7), Utils.toSnake(moduleMatcher.group(1)), "",
          if (schools.contains(Utils.toSnake(moduleMatcher.group(8)))) schools(Utils.toSnake(moduleMatcher.group(8))) else new School(0, "null", new Building(0, "null", ListBuffer())),
          ListBuffer(2),
          mutable.Set[RequiredSession]())
      }
    }

    modules.toSet
  }
}
