import junit.framework.TestCase
import org.junit.Assert._
import org.junit.Test

class Test extends TestCase{
  var testObj: Boolean = _

  override def setUp() = {
    testObj = true
  }

  def testWillFail{
    assertFalse(testObj)
  }

  def testWillPass{
    assertTrue(testObj)
  }
}