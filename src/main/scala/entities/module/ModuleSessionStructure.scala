package entities.module

class ModuleSessionStructure(
	var moduleSessionStructureId: Int,
	var weekNo: Int,
	var sessionType: ModuleSessionType,
	var noOfSessions: Int,
	var maxSessionSize: Int,
	var session: RequiredSession
)
