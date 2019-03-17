import junit.framework.TestCase
import org.junit.Assert.assertEquals
import org.junit.Before
import services.parser.TimeTableParser

class TimeTableParserTests extends TestCase{
  def testIfCorrectAmountOfModulesAreParsed = assertEquals(1917, TimeTableParser.modules.size)
  def testIfCorrectAmountOfSchoolsAreParsed = assertEquals(29, TimeTableParser.schools.size)
  def testIfAnyModuleNamesAreValid = assert(TimeTableParser.modules.values.exists(_.moduleName.matches("[A-Z0-9\\(\\),]+")))
  def testIfAnySchoolsArePopulated = assert(TimeTableParser.modules.values.exists(_.school != null))
}
