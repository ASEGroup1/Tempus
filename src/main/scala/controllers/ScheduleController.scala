package controllers

import javax.inject.{Inject, Singleton}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import services.scheduler.Scheduler

@Singleton
class ScheduleController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def test = Action {
    Ok(Json.parse(Scheduler.generateSchedule(100, 10).get.map(_.toJson).mkString("[", ",", "]"))).as("application/json")
  }
}
