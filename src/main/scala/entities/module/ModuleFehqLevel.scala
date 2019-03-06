package entities.module

class ModuleFehqLevel()

var baseModule: Module,
var fehqLevel: Int,
var prerequisites: ListBuffer[ModuleFehqLevel],
var courseAvailability: ListBuffer[Course],
var maxStudents: Int,
var modulePeople: ListBuffer[(ModuleRole, Person)],
var sessionStructure: ListBuffer[ModuleSessionStructure]
)
