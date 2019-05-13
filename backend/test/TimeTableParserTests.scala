import junit.framework.TestCase
import org.junit.Assert.assertEquals
import org.junit.Before
import services.parser.TimeTableParser._

class TimeTableParserTests extends TestCase{
  def testIfCorrectAmountOfModulesAreParsed = assertEquals(1916, modules.size)
  def testIfCorrectAmountOfSchoolsAreParsed = assertEquals(29, schools.size)
  def testIfAnyModuleNamesAreValid = assert(moduleMap.values.exists(_.moduleName.matches("[A-Z0-9\\(\\),]+")))
  def testIfAnySchoolsArePopulated = assert(moduleMap.values.exists(_.school != null))
}
