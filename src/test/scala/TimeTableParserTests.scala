import junit.framework.TestCase
import org.junit.Assert.assertEquals
import org.junit.Before
import services.parser.TimeTableParser

class TimeTableParserTests extends TestCase{
  def testIfCorrectAmountOfModulesAreParsed = assertEquals(1916, TimeTableParser.modules.size)
  def testIfCorrectAmountOfSchoolsAreParsed = assertEquals(29, TimeTableParser.schools.size)
  def testIfAnyModuleNamesAreValid = assert(TimeTableParser.moduleNames.values.exists(_.moduleName.matches("[A-Z0-9\\(\\),]+")))
  def testIfAnySchoolsArePopulated = assert(TimeTableParser.moduleNames.values.exists(_.school != null))
}
