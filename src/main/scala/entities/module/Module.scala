package entities.module

import entities.School

import scala.collection.mutable.ListBuffer

class Module(
	var moduleId: Int,
	var moduleCode: String,
	var moduleName: String,
	var moduleDescription: String,
	var school: School,
	var terms: ListBuffer[Int] // This signifies which terms the module is auto applied to
)
