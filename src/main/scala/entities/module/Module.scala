package entities.module

import java.sql.ResultSet

import entities.School
import services.JsonUtils._

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.util.Random

object Module {
  def apply(json:Map[String, Any]): Module = {
    var moduleId = extractInt(json("moduleId"))
    if(moduleId == null) moduleId = Random.nextInt

    new Module(moduleId, extractString(json("moduleCode")), extractString(json("moduleName")),
      extractString(json("moduleDescription")), null, ListBuffer(), mutable.Set())
  }

  def apply(qr: ResultSet): Module = new Module(qr.getInt(1), qr.getString(2), qr.getString(3), qr.getString(4), null, null, null)
}

class Module(
              var moduleId: Int,
              var moduleCode: String,
              var moduleName: String,
              var moduleDescription: String,
              var school: School,
              var terms: ListBuffer[Int], // This signifies which terms the module is auto applied to
              var requiredSessions: mutable.Set[RequiredSession]
            ) {
  def this() = this(0, "", "", "", null, null, null)
  def this(name:String) = this(0, "", name, "", null, null, null)


  def canEqual(other: Any): Boolean = other.isInstanceOf[Module]

  override def equals(other: Any): Boolean = other match {
    case that: Module =>
      (that canEqual this) &&
        moduleName == that.moduleName
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(moduleName)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}
