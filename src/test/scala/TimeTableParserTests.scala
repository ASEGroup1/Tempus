import junit.framework.TestCase
import org.junit.Assert.assertEquals
import services.parser.TimeTableParser

class TimeTableParserTests extends TestCase {
  def testModuleParsing() = {
    assertEquals(1917, TimeTableParser.modules.size)
  }

  def testSchoolsParsing() = {
    assertEquals(29, TimeTableParser.schools.size)
  }
}
