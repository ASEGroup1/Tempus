package services.scheduler.poso

import java.util.regex.Matcher

object Room {
  val intPatternString = "[0-9]+"

  def apply(m: Matcher): Room = {
    val capacity = if (m.group(3).matches(intPatternString)) m.group(3).toInt else -1
    val tableCount = if (m.group(4).matches(intPatternString)) m.group(4).toInt else -1
    val pcCount = if (m.group(7).matches(intPatternString)) m.group(7).toInt else -1

    new Room(m.group(1), m.group(2), capacity, tableCount, m.group(5), m.group(6), pcCount, m.group(8).toLowerCase().replace(" ", "") == "yes")
  }
}

//Group indexes: Lecture Theatre name: 0; capacity: 1; tables: 2; board: 3; laptop inputs: 4; pc count: 5; wheel chair access: 6
class Room(val name: String, val roomType:String, val capacity: Int, val tableCount: Int, val boardType: String,
           val laptopInputs: String, val pcCount: Int, val wheelchairAccess: Boolean) {
  def this(name: String) = this(name, "", 0, 0, "", "", 0, false)


  override def toString = name
}
