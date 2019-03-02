package entities.module

import entities.School

import scala.collection.mutable.ListBuffer

class Module {
  var moduleId: Int =_
  var moduleCode: String =_
  var moduleName: String = _
  var moduleDescription: String =_
  var school: School = _
  var terms: ListBuffer[Int] = _ // This signifies which terms the module is auto applied to
}
