package entities.module

import entities.{Generator, School}
import services.generator.modulegenerator.ModuleGenerator.{genInt, genStr}

import scala.collection.mutable.ListBuffer

object Module extends Generator[Module] {
	override def gen() = new Module(genInt, genStr, genStr, genStr, null, null)
}

class Module(
	var moduleId: Int,
	var moduleCode: String,
	var moduleName: String,
	var moduleDescription: String,
	var school: School,
	var terms: ListBuffer[Int] // This signifies which terms the module is auto applied to
)
