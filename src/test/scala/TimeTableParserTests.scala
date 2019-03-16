import junit.framework.TestCase
import org.junit.Assert.assertEquals
import services.parser.TimeTableParser

class TimeTableParserTests extends TestCase {
  def testModuleParsing() = {
    assertEquals(1689, TimeTableParser.modules.size)
  }

  def testIfModulesAreUnique() = {
    assertEquals(TimeTableParser.modules.groupBy(_.moduleName).size, TimeTableParser.modules.size)
  }

  def testSchoolsParsing() = {
    assertEquals(29, TimeTableParser.schools.size)
  }

  def testIfSchoolsAreUnique() = {
    assertEquals(TimeTableParser.schools.size, TimeTableParser.schools.groupBy(_.schoolName).size)
  }
}
