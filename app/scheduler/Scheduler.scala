package scheduler

import java.util.Calendar

import org.joda.time.LocalTime
import services.generator.eventgenerator.EventType.EventType

import scala.collection.mutable.HashMap


object Scheduler {

  // ATM just an ordered best fit bin packing
  def schedule(rooms: Seq[Room], events: Seq[Event], periods: Seq[Period]): Seq[ScheduledClass] = {
    // generate a map of all combinations of rooms and periods, and send that to the other method
    schedule(rooms.flatMap(r => periods.map(p => (r -> p))), events)
  }


  /**
    *
    * @param areas list of sequential time allowed in a room
    * @param events classes, etc.
    */
  def schedule(areas: Seq[(Room, Period)], events: Seq[Event]): Seq[ScheduledClass] ={
    // ordered best fit bin packing
    // packing "events" into "room" number of bins of size "period"
    val schedule = areas.map(a => new RoomSchedule(a._1, a._2))

    // for each event (largest first), find the smallest slot (room and time) that it fits in
    // Then add it to that slot
    events.sortBy(_.duration.getMillisOfDay)(Ordering[Int].reverse).foreach(e => {
      schedule.filterNot(_.timeRemaining<e.duration.getMillisOfDay).min(Ordering by[RoomSchedule, Int](s => s.timeRemaining)).addEvent(e)
    })

    schedule.flatMap(s => s.getSchedule())
  }

  private class RoomSchedule(val room:Room, val period: Period){
    var timeRemaining = period.duration.duration()
    var durationPointer = new LocalTime(period.duration.start)
    var events = new HashMap[Duration, Event]()

    def addEvent(event: Event): Unit ={
      var duration = event.duration.getMillisOfDay
      events += (new Duration(new LocalTime(durationPointer), new LocalTime(durationPointer.plusMillis(duration))) -> event)
      timeRemaining -= duration
      durationPointer.plusMillis(duration)
    }

    def getSchedule():Seq[ScheduledClass] = {
      events.map(e => new ScheduledClass(period, e._1, room, e._2)) toList
    }
  }

}

class Period(val calendar: Calendar, val duration: Duration)

class ScheduledClass(val day: Period, val time: Duration, val room: Room, val event: Event)

class Duration(val start: LocalTime, val end: LocalTime){
  def duration(): Int = {
    end.getMillisOfDay - start.getMillisOfDay
  }
}


class Building(val name: String)

class Room(val building: Building, val id: Int)


class Module(val id: Int, val name: String)

class Event(val id: Int, val name:String, val eventType:EventType, val duration: LocalTime)
