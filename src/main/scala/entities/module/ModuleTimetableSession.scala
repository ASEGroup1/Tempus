package entities.module

import entities.timing.TimePeriod

class ModuleTimetableSession {
	var timetableSessionId: Int = _
	var sessionType: ModuleSessionType = _
	var timePeriod: TimePeriod = _
	var dayNumber: Int = _  // As in which Day in the term period the session occurs on
}
