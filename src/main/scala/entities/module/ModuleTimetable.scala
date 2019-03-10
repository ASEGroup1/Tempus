package entities.module

import entities.Generator

import scala.collection.mutable.ListBuffer

object ModuleTimetable extends Generator[ModuleTimetable]{
	override def gen() = new ModuleTimetable(genInt, null)
}

class ModuleTimetable(
	var moduleTimetableId: Int,
	var sessions: ListBuffer[ModuleTimetableSession]
)
