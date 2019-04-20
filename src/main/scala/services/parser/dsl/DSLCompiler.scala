package services.parser.dsl


import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import services.scheduler.{ScheduleInterfaceMapper, Scheduler}

/**
  * Class used to force filters to only be generated once
  */
object FilterList {

  private var filters: Seq[(Seq[ScheduleInterfaceMapper], Seq[ScheduleInterfaceMapper]) => Seq[ScheduleInterfaceMapper]] = null

  def getFilters(): Seq[(Seq[ScheduleInterfaceMapper], Seq[ScheduleInterfaceMapper]) => Seq[ScheduleInterfaceMapper]] = {
    if (filters == null) {
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

  /**
    * Uses reflection to generate MethodReference's using fields of a class
    *
    * @param classPath        path to the target class
    * @param dslReferencePath path the dsl must specify to reach the class
    *                         E.g. if Array("a", "b") was specified, then to reference a member of the class m of a parameter p
    *                         p.a.b.m must be used.
    * @param isModule
    *                         If the module information will need to be extracted to read the value
    * @param ignore
    *                         List of fields that should be ignored
    * @return
    */
  private def mapTypes(classPath: String, dslReferencePath: Seq[String], isModule: Boolean, ignore: Set[String] = Set[String]()): Map[Seq[String], MethodReference] = {
    mapTypes(classPath, dslReferencePath, if (dslReferencePath.isEmpty) "" else (dslReferencePath.mkString(".") + "."), isModule, ignore)
  }

  /**
    * Uses reflection to generate MethodReference's using fields of a class
    *
    * @param classPath        path to the target class
    * @param dslReferencePath path the dsl must specify to reach the class
    *                         E.g. if Array("a", "b") was specified, then to reference a member of the class m of a parameter p
    *                         p.a.b.m must be used.
    * @param referencePath    path from ScheduleInterfaceMapper to reach the instance of the target class
    * @param isModule
    *                         If the module information will need to be extracted to read the value
    * @param ignore
    *                         List of fields that should be ignored
    * @return
    */
  private def mapTypes(classPath: String, dslReferencePath: Seq[String], referencePath: String, isModule: Boolean, ignore: Set[String]): Map[Seq[String], MethodReference] = {
    Class.forName(classPath).getDeclaredFields.filterNot(ignore contains _.getName).map(f =>
      new MethodReference(dslReferencePath ++ Array(f.getName), "%s." + referencePath + f.getName, isModule, f.getType).getMap()
    ).toMap
  }

  /**
    * Map of members of the parameters in the dsl in the form (path, reference).
    */
  val referenceTable: Map[Seq[String], MethodReference] = {
    val map = mutable.Map[Seq[String], MethodReference]()

    // primitiveTypes
    val stringType = "".getClass
    val intType = 0.getClass
    val boolType = true.getClass
    val floatType = 0.1f.getClass

    // Room
    map ++ mapTypes("entities.locations.Room", Array("room"), false, Set("roomType", "partitions", "serialVersionUID"))
    map += new MethodReference("room.type", "%s.room.roomType.name", stringType).getMap()

    // Time
    map += new MethodReference("start.hour", "%s.period.start.getHour", intType).getMap()
    map += new MethodReference("start.minute", "%s.period.start.getMinute", intType).getMap()
    map += new MethodReference("start.second", "%s.period.start.getSecond", intType).getMap()
    map += new MethodReference("start", "%s.period.start.getNano", intType).getMap()
    map += new MethodReference("end.hour", "%s.period.end.getHour", intType).getMap()
    map += new MethodReference("end.minute", "%s.period.end.getMinute", intType).getMap()
    map += new MethodReference("end.second", "%s.period.end.getSecond", intType).getMap()
    map += new MethodReference("end", "%s.period.end.getNano", intType).getMap()

    // Misc
    map += new MethodReference("scheduled", "%s.scheduled", boolType).getMap()
    map += new MethodReference("day", "%s.day", intType).getMap()

    // Schedule
    map += new MethodReference(Array("schedule", "timeRemaining"), "%s.roomSchedule.timeRemaining", false, floatType).getMap()
    map += new MethodReference("duration", "%s.event.duration", floatType).getMap()

    // Module Specific
    map += new MethodReference("week", "%s.weekNo", intType, true).getMap()
    map ++= mapTypes("entities.module.Module", Array("module"), "requiredSession.module.", true, Set("requiredSessions", "sessionStructure", "school", "terms"))
    map ++= mapTypes("entities.School", Array("school"), "requiredSession.module.school.", true, Set("mainBuilding"))

    map.toMap
  }

}

/**
  * Class to convert a FilterNode into a compiled function
  * @param filterNode to convert into a function
  */
class FilterCompiler(val filterNode: FilterNode) {

  /**
    * Will convert the where list to a single boolean expression
    * @param list
    * @return
    */
  private def crushWhereList(list: Seq[BooleanExpNode]) = list.reduce((l, r) => new BinaryOperationNode(l, r, BinOp.And))

  private val compiledFilter: CompiledFilter = {
    val bodyExp = new ExpressionCompiler(filterNode.body, filterNode.name.string + "Body", filterNode.param1)
    val whereExp = if (filterNode.where.isDefined) Some(new ExpressionCompiler(crushWhereList(filterNode.where.get.condition), filterNode.name.string + "Where", filterNode.param1)) else None
    if (filterNode.param2.isDefined) {
      val whereFunc: Option[(ScheduleInterfaceMapper, ScheduleInterfaceMapper) => Boolean] = if (whereExp.isDefined) Some(whereExp.get.compileDouble(filterNode.param1, filterNode.param2.get)) else None
      val bodyFunc: (ScheduleInterfaceMapper, ScheduleInterfaceMapper) => Boolean = bodyExp.compileDouble(filterNode.param1, filterNode.param2.get)
      if (!whereExp.isDefined || whereExp.get.isCommutative) {
        if (bodyExp.isCommutative) {
          new DoubleFilter(whereFunc, bodyFunc, 0)
        } else {
          new DoubleFilter(whereFunc, bodyFunc, 1)
        }
      } else {
        new DoubleFilter(whereFunc, bodyFunc, 2)
      }
    } else {
      new SingleFilter(if (whereExp.isDefined) applyWhereSingle(_,whereExp.get.compileSingle(filterNode.param1)) else applyWhereSingleNone(_), bodyExp.compileSingle(filterNode.param1))
    }
  }

  /**
    * Will repackage where, body functions using the respective wrapper functions in Scheduler
    */
  trait CompiledFilter {
    def compile: (Seq[ScheduleInterfaceMapper], Seq[ScheduleInterfaceMapper]) => Seq[ScheduleInterfaceMapper]
  }

  /**
    * CompiledFilter with only one parameter
    */
  class SingleFilter(val where: (Seq[ScheduleInterfaceMapper]) => Seq[ScheduleInterfaceMapper], val body: (ScheduleInterfaceMapper) => Boolean) extends CompiledFilter {
    override def compile: (Seq[ScheduleInterfaceMapper], Seq[ScheduleInterfaceMapper]) => Seq[ScheduleInterfaceMapper] = sWrap(_, _, where(_), body(_))
  }

  /**
    * CompiledFilter with two parameters
    */
  class DoubleFilter(val where: Option[(ScheduleInterfaceMapper, ScheduleInterfaceMapper) => Boolean], val body: (ScheduleInterfaceMapper, ScheduleInterfaceMapper) => Boolean, val t: Int) extends CompiledFilter {
    override def compile: (Seq[ScheduleInterfaceMapper], Seq[ScheduleInterfaceMapper]) => Seq[ScheduleInterfaceMapper] = {
      val whereFunc = if (where.isDefined) applyWhereDouble(_, _, where.get) else applyWhereDoubleNone(_, _)
      t match {
        case 0 =>
          RRWrap(_, _, whereFunc(_, _), body(_, _))
        case 1 =>
          RNWrap(_, _, whereFunc(_, _), body(_, _))
        case 2 =>
          if (!where.isDefined)
              throw new Exception("Cannot have NX filter, if where is not defined")
          NXWrap(_, _, whereFunc(_, _), applyWhereDoubleI(_, _, where.get),body(_, _))
        case _ =>
          throw new Exception("Invalid filter type")
      }
    }
  }


  private def applyWhereDouble(filledSlots: Seq[ScheduleInterfaceMapper], possibleSlots: Seq[ScheduleInterfaceMapper], where: (ScheduleInterfaceMapper, ScheduleInterfaceMapper) => Boolean):Seq[(ScheduleInterfaceMapper, Seq[ScheduleInterfaceMapper])] =
    possibleSlots.map(a => (a, filledSlots.filter(b =>
      where(a, b)
    )))

  private def applyWhereDoubleI(filledSlots: Seq[ScheduleInterfaceMapper], possibleSlots: Seq[ScheduleInterfaceMapper], where: (ScheduleInterfaceMapper, ScheduleInterfaceMapper) => Boolean):Seq[(ScheduleInterfaceMapper, Seq[ScheduleInterfaceMapper])] =
    possibleSlots.map(a => (a, filledSlots.filter(b =>
      where(b, a)
    )))

  private def applyWhereDoubleNone(filledSlots: Seq[ScheduleInterfaceMapper], possibleSlots: Seq[ScheduleInterfaceMapper]): Seq[(ScheduleInterfaceMapper, Seq[ScheduleInterfaceMapper])] = possibleSlots.map((_, filledSlots))

  /**
    * Function to process constraints where both the where and body functions are commutative.
    * i.e. where(a,b) = where(b,a) and body(a,b) = body(b,a).
    *
    * This function will remove all possibleSlots where there is an (a,b) where one is an instance of  filledSlots and the other of possibleSlots:
    * where(a,b) is true, and body(a,b) is false.
    *
    * @param filledSlots   currently scheduled events
    * @param possibleSlots events that could be scheduled
    * @param where
    * @param body
    * @return
    */
  private def RRWrap(filledSlots: Seq[ScheduleInterfaceMapper], possibleSlots: Seq[ScheduleInterfaceMapper], where: (Seq[ScheduleInterfaceMapper], Seq[ScheduleInterfaceMapper]) => Seq[(ScheduleInterfaceMapper, Seq[ScheduleInterfaceMapper])], body: (ScheduleInterfaceMapper, ScheduleInterfaceMapper) => Boolean): Seq[ScheduleInterfaceMapper] =
  // get applicable entries using the where
    possibleSlots.filterNot(where(filledSlots, possibleSlots).filterNot(g => {
      val a = g._1
      g._2.forall(b =>
        body(a, b)
      )
      // remove them and return
    }).toSet)


  /**
    * Function to process constraints where the where function is commutative.
    * i.e. where(a,b) = where(b,a) and body(a,b) = body(b,a).
    *
    * This function will remove all possibleSlots where there is an (a,b) where one is an instance of  filledSlots and the other of possibleSlots:
    * where(a,b) is true, and body(a,b) is false.
    *
    * @param filledSlots   currently scheduled events
    * @param possibleSlots events that could be scheduled
    * @param where
    * @param body
    * @return
    */
  private def RNWrap(filledSlots: Seq[ScheduleInterfaceMapper], possibleSlots: Seq[ScheduleInterfaceMapper], where: (Seq[ScheduleInterfaceMapper], Seq[ScheduleInterfaceMapper]) => Seq[(ScheduleInterfaceMapper, Seq[ScheduleInterfaceMapper])], body: (ScheduleInterfaceMapper, ScheduleInterfaceMapper) => Boolean): Seq[ScheduleInterfaceMapper] =
    // get applicable entries using the where
    possibleSlots.filterNot(where(filledSlots, possibleSlots).filterNot(g => {
      val a = g._1
      g._2.forall(b =>
        body(a, b) || body(b, a)
      )
      // remove them and return
    }).toSet)

  /**
    * Function to process constraints where the where function is non-commutative.
    * i.e. if where(a,b) is true does not mean where(b,a) is true
    *
    * This function will remove all possibleSlots where there is an (a,b) where one is an instance of  filledSlots and the other of possibleSlots:
    * where(a,b) is true, and body(a,b) is false.
    *
    * @param filledSlots   currently scheduled events
    * @param possibleSlots events that could be scheduled
    * @param where
    * @param body
    * @return
    */
  private def NXWrap(filledSlots: Seq[ScheduleInterfaceMapper], possibleSlots: Seq[ScheduleInterfaceMapper], where: (Seq[ScheduleInterfaceMapper], Seq[ScheduleInterfaceMapper]) => Seq[(ScheduleInterfaceMapper, Seq[ScheduleInterfaceMapper])], whereI: (Seq[ScheduleInterfaceMapper], Seq[ScheduleInterfaceMapper]) => Seq[(ScheduleInterfaceMapper, Seq[ScheduleInterfaceMapper])], body: (ScheduleInterfaceMapper, ScheduleInterfaceMapper) => Boolean): Seq[ScheduleInterfaceMapper] =
    // get applicable entries (a,b) using the where

    possibleSlots.filterNot((where(filledSlots, possibleSlots).filterNot(g => {
      val a = g._1
      g._2.forall(b =>
        body(a, b)
      )
      // get applicable entries (b,a) using the where
    }) ++ where(filledSlots, possibleSlots).filterNot(g => {
      val a = g._1
      g._2.forall(b =>
        body(b, a)
      )
      // remove them and return
    })).toSet)


  /**
    * Function to process constraints which use a single parameter
    * @param filledSlots currently scheduled events
    * @param possibleSlots events that could be scheduled
    * @param where
    * @param body
    * @return
    */
  private def sWrap(filledSlots: Seq[ScheduleInterfaceMapper], possibleSlots: Seq[ScheduleInterfaceMapper], where: (Seq[ScheduleInterfaceMapper]) => Seq[ScheduleInterfaceMapper], body: (ScheduleInterfaceMapper) => Boolean): Seq[ScheduleInterfaceMapper] =
    possibleSlots.filterNot(where(possibleSlots).filterNot(body(_)).toSet)

  private def applyWhereSingle(possibleSlots: Seq[ScheduleInterfaceMapper], where: (ScheduleInterfaceMapper) => Boolean): Seq[ScheduleInterfaceMapper] = possibleSlots.filter(where(_))
  private def applyWhereSingleNone(possibleSlots: Seq[ScheduleInterfaceMapper]): Seq[ScheduleInterfaceMapper] = possibleSlots


  def getFunction(): (Seq[ScheduleInterfaceMapper], Seq[ScheduleInterfaceMapper]) => Seq[ScheduleInterfaceMapper] =
    compiledFilter.compile
}

/**
  * Class to convert a BooleanExpNode into a compiled function
  * @param toCompile
  * @param name of the generated function
  * @param fallBackParam a parameter to use as a fallback, if the expression does not use any.
  */
class ExpressionCompiler(val toCompile: BooleanExpNode, val name: String, val fallBackParam: ParamNode) {
  import scala.reflect.runtime.universe._
  import scala.tools.reflect.ToolBox
  /**
    * Compile's the specified string into a function
    * @param code to compile
    * @return the compiled function
    */
  private def compileFunc(code: String): Any = {
    val tb = runtimeMirror(getClass.getClassLoader).mkToolBox()
    val f = tb.compile(tb.parse(code))
    f()
  }

  private var params = Set[ParamNode]()

  private var allExp = ListBuffer[BooleanExpNode]()

  /**
    * If the module information is used.
    * If this is true, the ScheduleInterfaceMapper's will need to be compared on a weekly basis.
    */
  var requiresModuleExtraction = false

  /**
    * Body of the function
    */
  private val body: String = compileBooleanExp(toCompile)

  /**
    * If the function is commutative.
    * A function f(_,_) is commutative if f(a,b) = f(b,a).
    * If the function is commutative then optimisations can be enforced that will reduce the number of calls
    */
  var isCommutative: Boolean = {
    // Todo: implement this method to improve efficiency
    params.size<2
  }

  /**
    * Generates the scala code for the function as a string.
    * @return the scala code
    */
  private def genFunctionString(): String = {
    val paramString = params.map(_.name.string + ": ScheduleInterfaceMapper").mkString(",")
    s"import services.scheduler.ScheduleInterfaceMapper\n def $name($paramString): Boolean = {$body}\n$name _"
  }

  def compileSingle(paramNode: ParamNode): (ScheduleInterfaceMapper) => Boolean = {
    if (params.size > 1) {
      throw new UnsupportedOperationException("Cannot compile function with two parameters to function with one")
    } else if (params.isEmpty) {
      params += paramNode
    }

    val func = compileFunc(genFunctionString()).asInstanceOf[(ScheduleInterfaceMapper) => Boolean]
    if (requiresModuleExtraction) mapEventsSingle(_,func(_)) else func
  }

  def compileDouble(paramNode1: ParamNode, paramNode2: ParamNode): (ScheduleInterfaceMapper, ScheduleInterfaceMapper) => Boolean = {
    if (params.size < 2) {
      params += paramNode1
      params += paramNode2
    }

    val func = compileFunc(genFunctionString()).asInstanceOf[(ScheduleInterfaceMapper, ScheduleInterfaceMapper) => Boolean]
    if (requiresModuleExtraction) mapEventsDouble(_,_,func(_,_)) else func
  }

  /**
    * A method that will allow functions to use indivdual module information.
    * This function exsits, as there is an overhead with adding the information.
    * @param a parameter
    * @param func function to apply
    * @return True, if func(a) is true, for every event in a
    */
  private def mapEventsSingle(a: ScheduleInterfaceMapper, func: (ScheduleInterfaceMapper) => Boolean): Boolean = a.event.events.map(a.withRequiredSession(_)).forall(func(_))

  /**
    * A method that will allow functions to use indivdual module information.
    * This function exsits, as there is an overhead with adding the information.
    * @param a first parameter
    * @param b second parameter
    * @param func function to apply
    * @return True, if func(a,b) is true, for every pair of events that run in the same week
    */
  private def mapEventsDouble(a: ScheduleInterfaceMapper, b: ScheduleInterfaceMapper, func: (ScheduleInterfaceMapper, ScheduleInterfaceMapper) => Boolean): Boolean = (
    // Get each pair of sessions that happen in the same week
    for {al <- a.event.events; bl <- b.event.events; if al._1 == bl._1}
    // add the sessions to the interfaceMapper
      yield (a.withRequiredSession(al), b.withRequiredSession(bl)))
    // Check the constraint holds for all pairs
    .forall(p => func(p._1, p._2))

  private def compileBooleanExp(booleanExpNode: BooleanExpNode): String = {
    booleanExpNode match {
      case e: BinaryOperationNode =>
        allExp += booleanExpNode
        "(" + compileBooleanExp(e.param1) + " " + (e.binOp match {
          case BinOp.Or => "||"
          case BinOp.And => "&&"
        }) + " " + compileBooleanExp(e.param1) + ")"
      case e: ComparisonNode =>
        allExp += booleanExpNode
        "(" + compileValue(e.param1) + " " + (e.comparator match {
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
        val str = new mutable.StringBuilder("if (" + branchConds.remove(0) + "){" + branchBodies.remove(0) + "} ")
        var depth = 0
        while (!branchConds.isEmpty) {
          str ++= "else { if (" + branchConds.remove(0) + "){" + branchBodies.remove(0) + "} "
          depth += 1
        }
        str ++= "else {" + compileBooleanExp(e.defaultBranch) + "}" + ("}" * depth)
        str.toString()
      case e: BooleanLiteralNode =>
        s"${e.bool}"
    }
  }

  private def compileValue(value: Value): String = {
    value match {
      case v: BooleanExpNode =>
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
    if (ref.isModule) {
      requiresModuleExtraction = true
    }

    referenceNode.variable.name.string.formatted(ref.codeReference)
  }
}

class MethodReference(val dslReference: Seq[String], val codeReference: String, val isModule: Boolean, val resolvedType: Class[_]) {
  def this(dslPath: String, codeReference: String, retType: Class[_], isModule: Boolean = false) = this(dslPath.split('.'), codeReference, isModule, retType)

  def getMap(): (Seq[String], MethodReference) = (dslReference, this)

  override def toString = s"MethodReference($dslReference, $codeReference, $isModule, $resolvedType)"
}