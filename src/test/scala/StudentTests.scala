import entities.people.Student
import junit.framework.TestCase
import org.junit.Assert.assertEquals

class StudentTests extends TestCase{
  def testIfModuleChoices(): Unit = assertEquals(100, Student.generate(100).size)
}
