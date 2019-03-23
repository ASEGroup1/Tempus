import junit.framework.TestCase
import org.junit.Assert._
import services.scheduler.Scheduler

class SchedulerTests extends TestCase {

  def testIfScheduleIncludesAllEvents =
    assertEquals(1917, Scheduler.binPackSchedule(5).get.size)

  def testIfNoEventsGenerateEmptySchedule = assertEquals(None,  Scheduler.binPackSchedule(0))

  def testIfEventsCannotFitInSchedule: Unit = {
    assertEquals(None,  Scheduler.binPackSchedule(1))
  }
}
