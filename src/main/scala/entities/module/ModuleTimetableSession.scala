package entities.module

import entities.timing.TimePeriod

class ModuleTimetableSession(
	var timetableSessionId: Int,
	var sessionType: ModuleSessionType,
	var timePeriod: TimePeriod,
	var dayNumber: Int // As in which Day in the term period the session occurs on
)
