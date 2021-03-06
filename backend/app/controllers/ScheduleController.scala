package controllers

import db.TimeTableDao
import javax.inject.{Inject, Singleton}
import org.json4s._
import org.json4s.native.Serialization
import org.json4s.native.Serialization._
import play.api.mvc.{AbstractController, ControllerComponents}
import services.Utils._
import services.parser.TimeTableParser
import services.scheduler.Scheduler
import services.scheduler.poso.ScheduledClass
import services.sussexroomscraper.SussexRoomScraper

@Singleton
class ScheduleController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  implicit val formats = Serialization.formats(NoTypeHints)
  var lastTimetableStr = Map[String, Iterable[List[String]]]()
  var timetable = Option(List[ScheduledClass]())

  val moduleNames = TimeTableParser.getGeneratedStudentsModuleNames

  def generateScheduleForRoomTable = Action {
    timetable = Scheduler.binPackSchedule(SussexRoomScraper.roomDataForSession, TimeTableParser.modules)
    if(timetable.isEmpty) Ok(write(false))
    else Ok(write(scheduleToRoomJson(timetable.get)))
  }

  def generateScheduleForStudentTable = Action {
    if(timetable.isEmpty) Ok(write(false))
    else Ok(write(scheduleToStudentJson(timetable.get
      .filter(sc => moduleNames.contains(sc.className)).sortBy(sc => (sc.day.calendar, sc.time.start)), TimeTableParser.getGeneratedStudentsModuleNames)))
  }

  def scheduleToRoomJson(schedule: List[ScheduledClass]) = {
    //Converts into list of strings with string count corresponding to length in hours, this is necessary input for table
    def getStringCountCorrespondingToLength(scheduledClass: ScheduledClass) =
      for (_ <- 0 until scheduledClass.time.end.getHour - scheduledClass.time.start.getHour)
        yield toNatLang(scheduledClass.className)

    //Room schedules
    lastTimetableStr = schedule.sortBy(sc => (sc.day.calendar, sc.time.start)).groupBy(_.room)
      //Room schedules by day so 2D array is created
      .map(ss => ss._1.roomName -> ss._2.groupBy(_.day.calendar.getDayOfYear).map(_._2.flatMap(getStringCountCorrespondingToLength)))
    lastTimetableStr
  }

  def scheduleToStudentJson(schedule: List[ScheduledClass], moduleNames: List[String]) = {
    def getSessionName(bounds: List[(Int, Int, String)], time: Int) = {
      val intersectingSessions = bounds.filter(b => b._1 < time && b._2 > time)
      if(intersectingSessions.isEmpty) "" else toNatLang(intersectingSessions.head._3)
    }

    //Creates bounds for each session
    schedule.groupBy(_.day.calendar.getDayOfYear).map(day => day._2.map(sc => (sc.time.start.getHour, sc.time.end.getHour, sc.className)))
      //populates timetable with session if the time intersects, otherwise with nothing
      .map(dayBounds => for (time <- 8 to 20) yield getSessionName(dayBounds, time))
  }

  def saveSchedule(name: String) = Action {
    try {
      TimeTableDao.insert(lastTimetableStr, name)
      Ok(s"Inserted timetable: $name")
    } catch {case e: Exception => BadRequest(if(e.getMessage.length > 100) e.getMessage.substring(0, 100) else e.getMessage)}
  }

  def getTimetableNames = Action {Ok(write(TimeTableDao.getTimetableNames))}

  def get(name: String) = Action {
    Ok(write(TimeTableDao.get(name)))
  }
}
