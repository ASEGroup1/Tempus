package entities.module

import scala.collection.mutable.ListBuffer

class ModuleTimetable {
	var moduleTimetableId: Int = _
	var sessions: ListBuffer[ModuleTimetableSession] = _
}
