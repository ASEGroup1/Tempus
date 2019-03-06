package entities.module

import scala.collection.mutable.ListBuffer

class ModuleTimetable(
	var moduleTimetableId: Int,
	var sessions: ListBuffer[ModuleTimetableSession]
)
