package services.parser.dsl


import services.parser.dsl.DSLCompiler.getClass

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.reflect.runtime.universe._
import scala.tools.reflect.ToolBox
import services.scheduler.{ScheduleInterfaceMapper, Scheduler}


object FilterList{

  var filters: Seq[(Seq[ScheduleInterfaceMapper], Seq[ScheduleInterfaceMapper]) => Seq[ScheduleInterfaceMapper]] = null

  def getFilters(): Seq[(Seq[ScheduleInterfaceMapper], Seq[ScheduleInterfaceMapper]) => Seq[ScheduleInterfaceMapper]] = {
    if(filters == null){
      val dsl = "filter WillFit(e){\n\te.schedule.timeRemaining >= e.duration\n}"

      filters = DSLCompiler.compile(dsl)
    }
    print("getFilters")
    filters
  }
}



object DSLCompiler {


  def compile(code: String): Seq[(Seq[ScheduleInterfaceMapper], Seq[ScheduleInterfaceMapper]) => Seq[ScheduleInterfaceMapper]] =
     DSLParser.parse(DSLLexer.lex(code)).map(new FilterCompiler(_).getFunction())

  def main(args: Array[String]): Unit = {

    val dsl = "filter WillFit(e){\n\te.schedule.timeRemaining >= e.duration\n}"
    val lexed = DSLLexer.lex(dsl)
    println(lexed)
    val parsed = DSLParser.parse(lexed)
    println(parsed)

    val filter = new FilterCompiler(parsed(0))


    println(filter)
  }

  private def mapTypes(classPath: String, dslReferencePath: Seq[String], isModule: Boolean, ignore: Set[String] = Set[String]()): Map[Seq[String], MethodReference] = {
    mapTypes(classPath, dslReferencePath, if (dslReferencePath.isEmpty) "" else (dslReferencePath.mkString(".") + "."), isModule, ignore)
  }

  private def mapTypes(classPath: String, dslReferencePath: Seq[String], referencePath: String, isModule: Boolean, ignore: Set[String]): Map[Seq[String], MethodReference]= {
    Class.forName(classPath).getDeclaredFields.filterNot(ignore contains _.getName).map(f =>
      new MethodReference(dslReferencePath ++ Array(f.getName), "%s."+referencePath+f.getName, isModule, f.getType).getMap()
    ).toMap
  }

  val referenceTable: Map[Seq[String], MethodReference] = {
    val map = mutable.Map[Seq[String], MethodReference]()

    // primitiveTypes
    val stringType = "".getClass
    val intType = 0.getClass
    val boolType = true.getClass
    val floatType = 0.1f.getClass

    // Room
    map ++ mapTypes("entities.locations.Room", Array("room"),false, Set("roomType", "partitions", "serialVersionUID"))
    map += new MethodReference(Array("room", "type"), "%s.room.roomType.name", false, stringType).getMap()

    // Time
    map += new MethodReference(Array("start", "hour"), "%s.period.start.getHour", false, intType).getMap()
    map += new MethodReference(Array("start", "minute"), "%s.period.start.getMinute", false, intType).getMap()
    map += new MethodReference(Array("start", "second"), "%s.period.start.getSecond", false, intType).getMap()
    map += new MethodReference(Array("start"), "%s.period.start.getNano", false, intType).getMap()
    map += new MethodReference(Array("end", "hour"), "%s.period.end.getHour", false, intType).getMap()
    map += new MethodReference(Array("end", "minute"), "%s.period.end.getMinute", false, intType).getMap()
    map += new MethodReference(Array("end", "second"), "%s.period.end.getSecond", false, intType).getMap()
    map += new MethodReference(Array("end"), "%s.period.end.getNano", false, intType).getMap()

    // Misc
    map += new MethodReference(Array("scheduled"), "%s.scheduled", false, boolType).getMap()
    map += new MethodReference(Array("day"), "%s.day", false, intType).getMap()

    // Schedule
    map += new MethodReference(Array("schedule", "timeRemaining"), "%s.roomSchedule.timeRemaining", false, floatType).getMap()

    // Module Specific
    map += new MethodReference(Array("week"), "%s.weekNo", true, intType).getMap()
    map += new MethodReference(Array("duration"), "%s.event.duration", true, floatType).getMap()
    map ++= mapTypes("entities.module.Module", Array("module"), "requiredSession.module.", true, Set("requiredSessions", "sessionStructure", "school", "terms"))
    map ++= mapTypes("entities.School", Array("school"), "requiredSession.module.school.", true, Set("mainBuilding"))

    //map.map(_._2).map(m => "p." + m.dslReference.mkString(".") +  ": " + m.resolvedType).foreach(println(_))


    map.toMap
  }

}

class FilterCompiler(val filterNode: FilterNode){
  // if there is no where defined, then body can be moved to where



  val bodyExp = new ExpressionCompiler(filterNode.body, filterNode.name.string+"Body", filterNode.param1)
  val whereExp = new ExpressionCompiler(if (filterNode.where.isDefined) crushWhereList(filterNode.where.get.condition) else new BooleanLiteralNode(true), filterNode.name.string+"Where", filterNode.param1)

  val compiledFilter: CompiledFilter = {
    if (filterNode.param2.isDefined){
      val whereFunc: (ScheduleInterfaceMapper, ScheduleInterfaceMapper) => Boolean = whereExp.compileDouble(filterNode.param1, filterNode.param2.get)
      val bodyFunc: (ScheduleInterfaceMapper, ScheduleInterfaceMapper) => Boolean = bodyExp.compileDouble(filterNode.param1, filterNode.param2.get)
      if(whereExp.isCommutative){
        if(bodyExp.isCommutative){
          new DoubleFilter(whereFunc, bodyFunc, 0)
        }else{
          new DoubleFilter(whereFunc, bodyFunc, 1)
        }
      }else{
        new DoubleFilter(whereFunc, bodyFunc, 2)
      }
    }else{
      new SingleFilter(whereExp.compileSingle(filterNode.param1),bodyExp.compileSingle(filterNode.param1))
    }
  }


  trait CompiledFilter{
    def compile:(Seq[ScheduleInterfaceMapper], Seq[ScheduleInterfaceMapper]) => Seq[ScheduleInterfaceMapper]
  }

  class SingleFilter(val where: (ScheduleInterfaceMapper) => Boolean, val body: (ScheduleInterfaceMapper) => Boolean) extends CompiledFilter {
    override def compile: (Seq[ScheduleInterfaceMapper], Seq[ScheduleInterfaceMapper]) => Seq[ScheduleInterfaceMapper] = Scheduler.sWrap(_,_,where(_), body(_))
  }

  class DoubleFilter(val where: (ScheduleInterfaceMapper, ScheduleInterfaceMapper) => Boolean, val body: (ScheduleInterfaceMapper, ScheduleInterfaceMapper) => Boolean, val t : Int) extends CompiledFilter {
    override def compile: (Seq[ScheduleInterfaceMapper], Seq[ScheduleInterfaceMapper]) => Seq[ScheduleInterfaceMapper] = {
      t match {
        case 0 =>
          Scheduler.RRWrap(_, _, where(_, _), body(_, _))
        case 1 =>
          Scheduler.RNWrap(_, _, where(_, _), body(_, _))
        case 2 =>
          Scheduler.NRNNWrap(_,_,where(_,_), body(_,_))
        case _ =>
          throw new Exception("Invalid filter type")
      }
    }
  }

  def getFunction():(Seq[ScheduleInterfaceMapper], Seq[ScheduleInterfaceMapper]) => Seq[ScheduleInterfaceMapper] = {
    compiledFilter.compile
  }


  // convert where argument list into single and
  def crushWhereList(list: Seq[BooleanExpNode]) = list.reduce((l,r) => new BinaryOperationNode(l,r, BinOp.And))
}

class ExpressionCompiler(val toCompile: BooleanExpNode, val name: String, val fallBackParam: ParamNode){
  def compileFunc[I](code: String): Any = {
    val tb = runtimeMirror(getClass.getClassLoader).mkToolBox()
    val f = tb.compile(tb.parse(code))
    print("comp")
    f()
  }

  var params = Set[ParamNode]()

  var allExp = ListBuffer[BooleanExpNode]()

  var requiresModuleExtraction = false

  var body: String = compileBooleanExp(toCompile)

  var isCommutative: Boolean = checkCommutativity()

  var functionText1: String = {
    if(params.isEmpty){
      // keep one param to keep interface
      params += fallBackParam
    }
    isCommutative = checkCommutativity()

    val paramString = params.map(_.name.string+": ScheduleInterfaceMapper").mkString(",")
    s"import services.scheduler.ScheduleInterfaceMapper\n def $name($paramString): Boolean = {$body}"
  }

  def genText():String = {
    val paramString = params.map(_.name.string+": ScheduleInterfaceMapper").mkString(",")
    s"import services.scheduler.ScheduleInterfaceMapper\n def $name($paramString): Boolean = {$body}\n$name _"
  }

  def compileSingle(paramNode: ParamNode): (ScheduleInterfaceMapper) => Boolean ={
    if(params.size>1){
      throw new UnsupportedOperationException("Cannot compile function with two parameters to function with one")
    }else if(params.isEmpty){
      params += paramNode
    }

    compileFunc(genText()).asInstanceOf[(ScheduleInterfaceMapper) => Boolean]
  }

  def compileDouble(paramNode1: ParamNode, paramNode2: ParamNode): (ScheduleInterfaceMapper, ScheduleInterfaceMapper) => Boolean ={
    if(params.size<2){
      params += paramNode1
      params += paramNode2
    }

    compileFunc(genText()).asInstanceOf[(ScheduleInterfaceMapper, ScheduleInterfaceMapper) => Boolean]
  }

  def checkCommutativity(): Boolean = {
    // To be implemented

    false
  }

  private def compileBooleanExp(booleanExpNode: BooleanExpNode): String = {
    booleanExpNode match {
      case e: BinaryOperationNode =>
        allExp += booleanExpNode
        "("+ compileBooleanExp(e.param1) + " " + (e.binOp match {
          case BinOp.Or => "||"
          case BinOp.And => "&&"
        }) + " " + compileBooleanExp(e.param1) + ")"
      case e: ComparisonNode =>
        allExp+= booleanExpNode
        "("+compileValue(e.param1) + " " + (e.comparator match {
          case services.parser.dsl.Comparator.Eq =>
            "=="
          case services.parser.dsl.Comparator.Ne =>
            "!="
          case services.parser.dsl.Comparator.Gt =>
            ">"
          case services.parser.dsl.Comparator.Lt =>
            "<"
          case services.parser.dsl.Comparator.Ge =>
            ">="
          case services.parser.dsl.Comparator.Le =>
            "<="
        }) + " " + compileValue(e.param2) + ")"
      case e: NegNode =>
        "!(" + compileBooleanExp(e.expr) + ")"
      case e: ConditionalNode =>
        val branchConds = ListBuffer[String]()
        val branchBodies = ListBuffer[String]()
        e.branches.foreach(b => {
          branchConds += compileBooleanExp(b.condition)
          branchBodies += compileBooleanExp(b.body)
        })
        val str = new mutable.StringBuilder("if (" + branchConds.remove(0) + "){"+branchBodies.remove(0)+"} ")
        var depth = 0
        while(!branchConds.isEmpty){
          str ++= "else { if (" + branchConds.remove(0) + "){"+branchBodies.remove(0)+"} "
          depth += 1
        }
        str ++= "else {" + compileBooleanExp(e.defaultBranch) + "}" + ("}"*depth)
        str.toString()
      case e: BooleanLiteralNode =>
        s"${e.bool}"
    }
  }

  private def compileValue(value: Value): String = {
    value match{
      case v:BooleanExpNode =>
        compileBooleanExp(v)
      case v: ReferenceNode =>
        resolveReference(v)
      case v: IntegerLiteralNode =>
        s"${v.int}"
      case v: FloatLiteralNode =>
        s"${v.float}"
      case v: StringLiteralNode =>
        s""""${v.string}""""
    }
  }

  private def resolveReference(referenceNode: ReferenceNode): String = {

    val ref = DSLCompiler.referenceTable(referenceNode.parts.map(_.string))

    params += referenceNode.variable
    if(ref.isModule){
      requiresModuleExtraction = true
    }

    referenceNode.variable.name.string + ref.codeReference.drop(2)
  }
}

class MethodReference(val dslReference: Seq[String], val codeReference: String, val isModule: Boolean, val resolvedType: Class[_]){
  def getMap():(Seq[String], MethodReference) = (dslReference, this)

  override def toString = s"MethodReference($dslReference, $codeReference, $isModule, $resolvedType)"
}