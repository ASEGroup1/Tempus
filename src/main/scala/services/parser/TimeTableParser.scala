package services.parser

import java.time.{LocalDate, LocalDateTime, LocalTime}
import java.util.regex.Pattern

import entities.School
import entities.module.{Module, RequiredSession}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.io.Source

object TimeTableParser {
  val ModulePattern = Pattern.compile("[5-9]L([\\S\\s]*?)\\([A-Z0-9]+\\)/[0-9]+,([A-Za-z]+),([0-9]{1,2}:[0-9]{1,2}),([0-9]{1,2}:[0-9]{1,2}),([\\S\\s]*?),[\\S\\s]*?,([\\S\\s]*?),([\\S\\s]*?),")

  val timeTablecsvStr = Source.fromFile(getClass.getResource("/input/Gregory_Mitten_Full_Timetable.csv").getPath).mkString

  def parseModules(): mutable.Set[Module] = {
    var modules = mutable.Set[Module]()
    val moduleMatcher = ModulePattern.matcher(timeTablecsvStr)
    var currentModule = new Module()
    var sessions = mutable.Set[RequiredSession]()

    while (moduleMatcher.find) {
      sessions += new RequiredSession(0, moduleMatcher.group(4).split(":")(0).toInt - moduleMatcher.group(3).split(":")(0).toInt)
      if (moduleMatcher.group(1) != currentModule.moduleName) {
        if (!currentModule.moduleName.isEmpty) {
          currentModule.requiredSessions = sessions
          modules += currentModule
          sessions = mutable.Set[RequiredSession]()
        }
        currentModule = new Module(0, moduleMatcher.group(6), moduleMatcher.group(1), "",
          new School(0, moduleMatcher.group(7), null),
          ListBuffer(2),
          null)
      }
    }
    modules
  }
}
