package services.parser

import java.util.regex.Pattern

import entities.School
import entities.locations.Building
import entities.module._
import services.Utils

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.util.Random

object TimeTableParser {
  val TimeTablePattern = Pattern.compile("\\\"?[0-9]L[\\s]([\\S\\s]*?)\\([\\S\\s]+?\\)\\/[0-9]+\\\"?,([A-Za-z]+),([0-9]{1,2}:[0-9]{1,2}),([0-9]{1,2}:[0-9]{1,2}),([\\S\\s]*?),([10]*?),([\\S\\s]*?),([\\S\\s]*?),")
  val SchoolPattern = Pattern.compile("[A-Z][0-9]{4},([\\S\\s]*?),")

  val timeTableCsvStr = Source.fromFile(getClass.getResource("/input/Gregory_Mitten_Full_Timetable.csv").getPath).mkString
  val studentCsvStr = Source.fromFile(getClass.getResource("/input/Pathways.csv").getPath).mkString

  val schools: Map[String, School] = {
    val schoolMatcher = SchoolPattern.matcher(timeTableCsvStr)
    var schools = mutable.Set[String]()
    while (schoolMatcher.find) {
      schools += Utils.toSnake(schoolMatcher.group(1))
    }
    schools.filter(!_.contains("(")).zipWithIndex.map(s => s._1 -> new School(s._2, s._1, null)).toMap
  }

  def getGeneratedStudentsModuleNames = {
    val schlName = schools.keys.toList(Random.nextInt(schools.keys.size - 1))

    def takeModulesFromTerm(term: Int) =
      Random.shuffle(modules.filter(m => m.school.schoolName == schlName && m.terms.nonEmpty && m.terms.head == term)).take(4).map(_.moduleName)

    (1 to 3).flatMap(takeModulesFromTerm).toList
  }

  val moduleMap: Map[String, Module] = {
    val modules = mutable.Map[String, Module]()
    val moduleMatcher = TimeTablePattern.matcher(timeTableCsvStr)

    var moduleId = 0
    var sessionId = 0
    var mssId = 0


    val nullModuleSessionType = new ModuleSessionType(0, "Null", "Null")
    val nullInt = 0
    val nullSchool = new School(0, "null", new Building(0, "null", ListBuffer()))

    while (moduleMatcher.find) {
      def getTerms(wkPtn: String): ListBuffer[Int] = {
        var terms = ListBuffer[Int]()

        if(wkPtn.substring(0, 12).contains('1')) terms += 1
        if(wkPtn.substring(19, 32).contains('1')) terms += 2
        if(wkPtn.substring(32, wkPtn.length - 1).contains('1')) terms += 3

        terms
      }

      // get or create the module
      val currentModule = modules.get(Utils.toSnake(moduleMatcher.group(1))) match {
        case Some(module) =>
          module
        case None =>
          // create the module
          val m = new Module({
            moduleId += 1
            moduleId
          }, moduleMatcher.group(7), Utils.toSnake(moduleMatcher.group(1)), "",
            if (schools.contains(Utils.toSnake(moduleMatcher.group(8)))) schools(Utils.toSnake(moduleMatcher.group(8))) else nullSchool,
            getTerms(moduleMatcher.group(6)),
            mutable.Set[RequiredSession](), ListBuffer[ModuleSessionStructure](), Random.nextInt(Module.num), Random.nextInt(Module.disMax) > Module.disPer)
          // add the module to the set
          modules += (m.moduleName -> m)
          m
      }

      // read required session from the entry
      val session = new RequiredSession({
        sessionId += 1
        sessionId
      }, moduleMatcher.group(4).split(":")(0).toInt - moduleMatcher.group(3).split(":")(0).toInt, currentModule)

      // add required session
      currentModule.requiredSessions += session

      // add session structure of required session
      currentModule.sessionStructure ++= moduleMatcher.group(6).zipWithIndex.filter(_._1 == '1').map(_._2).map(w => new ModuleSessionStructure({
        mssId += 1; mssId
      }, w,
        nullModuleSessionType, 1, nullInt, session))
    }

    modules.toMap
  }

  val modules: Set[Module] = moduleMap.values.toSet
}
