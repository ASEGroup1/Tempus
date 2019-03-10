package entities.module

import entities.Generator
import entities.timing.TimePeriod

object ModuleTimetableSession extends Generator[ModuleTimetableSession] {
	override def gen() = new ModuleTimetableSession(genInt, null, null, genInt)
}


class ModuleTimetableSession(
	var timetableSessionId: Int,
	var sessionType: ModuleSessionType,
	var timePeriod: TimePeriod,
	var dayNumber: Int // As in which Day in the term period the session occurs on
)
