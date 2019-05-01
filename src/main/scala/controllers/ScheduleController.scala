package controllers

import java.time.temporal.ChronoField

import org.json4s._
import javax.inject.{Inject, Singleton}
import org.json4s.native.Serialization
import org.json4s.native.Serialization._
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import services.Utils
import services.parser.TimeTableParser
import services.scheduler.Scheduler
import services.scheduler.poso.ScheduledClass
import services.sussexroomscraper.SussexRoomScraper
import views.ErrorPage


@Singleton
class ScheduleController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  implicit val formats = Serialization.formats(NoTypeHints)

  def generateScheduleResponse = Action {
    val schedule = Scheduler.binPackSchedule(5, SussexRoomScraper.roomDataForSession, TimeTableParser.modules)

    if(schedule.isEmpty) BadRequest(ErrorPage.badRequest("Could not generate, refresh for new random parameters.")).as("text/html")
    else Ok (Json.parse(schedule.get.sortBy(e => (e.day.calendar.getDayOfYear, e.room.roomName, e.time.start.get(ChronoField.MILLI_OF_DAY))).map(_.toJson).mkString("[", ",", "]"))).as("application/json")
  }

  def generateScheduleForTable = Action {
    Ok(write(scheduleToJson(Scheduler.binPackSchedule(5, SussexRoomScraper.roomDataForSession, TimeTableParser.modules).get)))
  }

  def scheduleToJson(schedule: List[ScheduledClass]) = {
    //Converts into list of strings with string count corresponding to length in hours, this is necessary input for table
    def getStringCountCorrespondingToLength(scheduledClass: ScheduledClass) =
      for (_ <- 0 until scheduledClass.time.end.getHour - scheduledClass.time.start.getHour)
        yield Utils.toNatLang(scheduledClass.className)

    //Room schedules
    schedule.sortBy(sc => (sc.day.calendar, sc.time.start)).groupBy(_.room)
      //Room schedules by day so 2D array is created
      .map(ss => ss._1.roomName -> ss._2.groupBy(_.day.calendar.getDayOfYear).map(_._2.flatMap(getStringCountCorrespondingToLength)))
  }
}
