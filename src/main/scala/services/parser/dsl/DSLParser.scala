package services.parser.dsl

import services.parser.dsl.BinOp.BinOp
import ASTVisitor._
import services.parser.dsl.Comparator.Comparator

import scala.collection.mutable.ListBuffer


object DSLParser {
  // Max number of constraints that can be defined in where
  val WHERE_MAX_EXP = 16

  // Parameters of the current filters
  var params: Option[Map[String, ParamNode]] = None

  def parse(tokens: Seq[Symbol]): Seq[FilterNode] = {
    val filters = ListBuffer[FilterNode]()

    val tokenStream: ListBuffer[Symbol] = tokens.to[ListBuffer]
    while (!tokenStream.isEmpty){
      filters += FilterNode.visit(tokenStream)
    }

    filters
  }
}

final case class ParserException(private val message: String = "Malformed DSL",
                                 private val cause: Throwable = None.orNull)
  extends Exception(message, cause)


sealed trait ASTNode
private object ASTVisitor{

  /**
    * Checks the next symbol is an instance of the specified type.
    * If the symbol is not a member of the specified type, a ParserException will be thrown
    * @param tokens to be tested
    * @param target type, expected to be next
    * @tparam T
    * @return true if the next token is the one expected
    */
  def assertNext[T <: Symbol](tokens:Seq[Symbol], target:T) = {
    if(tokens(0).isInstanceOf[target.type]){
      true
    }else{
      throw new ParserException("Unexpected symbol: "+ tokens(0))
    }
  }

  /**
    * Retrieves the next symbol as an instance of the specified type
    * @param tokens
    * @param target
    * @tparam T
    * @return
    */
  def getAs[T <: Symbol](tokens:Seq[Symbol], target:T): T = tokens(0).asInstanceOf[T]

  /**
    * Checks the next symbol is an instance of the specified type, and removes it.
    * If the symbol is not a member of the specified type, a ParserException will be thrown.
    *
    * @param tokens
    * @param target
    * @tparam T
    * @return
    */
  def popSymbol[T <: Symbol](tokens: ListBuffer[Symbol], target: T): ListBuffer[Symbol] = {
    assertNext(tokens,target)
    tokens.remove(0)
    tokens
  }

  /**
    * Will extract the section of tokens from open to close, respecting depth.
    * If the initial symbol is not an instance of open, or there is no subsection 0-i in tokens where sub.count(open) == sub.count(close), a ParserException will be thrown
    * @param tokens
    * @param open
    * @param close
    * @return
    */
  private def extractSegment(tokens: ListBuffer[Symbol], open: Symbol, close:Symbol): ListBuffer[Symbol] = {
    assertNext(tokens, open)
    var depth = 0
    var i = 0
    tokens.foreach(t => {
      i += 1
      if (t.equals(open)){
        depth += 1
      }else if (t.equals(close)){
        depth -= 1
      }
      if (depth == 0){
        val ret = tokens.take(i)
        tokens --= ret
        return ret.drop(1).dropRight(1)
      }
    })
    throw new ParserException(s"""Could not find closing "$close" in "$tokens" """)
  }

  /**
    * Will extract the next section of brackets "(", ")" respecting depth
    * @param tokens
    * @return
    */
  def extractBracket(tokens:ListBuffer[Symbol]) = extractSegment(tokens, OPENBRACKET, CLOSEBRACKET)

  /**
    * Will extract the next section of braces "{", "}" respecting depth
    * @param tokens
    * @return
    */
  def extractBrace(tokens:ListBuffer[Symbol]) = extractSegment(tokens, OPENBRACE, CLOSEBRACE)
}

// Types
trait Value extends ASTNode
private object Value {
  def visit(tokens: ListBuffer[Symbol]): Value = {
    tokens(0) match {
      case INTLITERAL(int) =>
        new IntegerLiteralNode(int)
      case CHARLITERAL(c) =>
        new CharLiteralNode(c)
      case FLOATLITERAL(f) =>
        new FloatLiteralNode(f)
      case BOOLEANLITERAL(b) =>
        new BooleanLiteralNode(b)
      case _ =>
          val parts = ListBuffer[StringLiteralNode]()
          var loop = true
          while(loop) {
            parts += new StringLiteralNode(tokens(0) match {
              case UNCAPWORD(s) => s
              case CAPWORD(s) => s
              case _ => throw new ParserException("Invalid value: " + tokens)
            })
            tokens.remove(0)
            if(tokens.isEmpty || !tokens(0).equals(DOT)){
              loop = false
            }else{
              tokens.remove(0)
            }
          }
          parts.size match {
            case 1 => parts(0)
            case x if x > 1 && DSLParser.params.isDefined && DSLParser.params.get.contains(parts(0).string) =>
              new ReferenceNode(DSLParser.params.get(parts(0).string), parts.drop(1))
            case _ => throw new ParserException("Invalid value: " + tokens)
          }
    }
  }
}

trait BooleanExpNode extends Value
private object BooleanExpNode {
  def visit(tokens: ListBuffer[Symbol]): BooleanExpNode = {
    tokens(0) match{
      case NOT =>
        tokens.remove(0)
        new NegNode(BooleanExpNode.visit(tokens))
      case BOOLEANLITERAL(b) =>
        tokens.remove(0)
        new BooleanLiteralNode(b)
      case IF =>
        ConditionalNode.visit(tokens)
      case OPENBRACKET =>
        val extract = extractBracket(tokens)
        BooleanExpNode.visit(extract)
      case _ =>
        val left = Value.visit(tokens)
        val (op, isBinOp): (Any, Boolean) = tokens.remove(0) match{
          case OR =>
            (BinOp.Or,true)
          case AND =>
            (BinOp.And,true)
          case EQ =>
            (Comparator.Eq, false)
          case NE =>
            (Comparator.Ne, false)
          case GE =>
            (Comparator.Ge, false)
          case GT =>
            (Comparator.Gt, false)
          case LE =>
            (Comparator.Le, false)
          case LT =>
            (Comparator.Lt, false)
          case _ =>
              throw new ParserException("""Invalid operation: "$left $op" """)
        }
        if (isBinOp){
          val right = BooleanExpNode.visit(tokens)
          new BinaryOperationNode(left.asInstanceOf[BooleanExpNode], right, op.asInstanceOf[BinOp])
        }else {
          val right = Value.visit(tokens)
          new ComparisonNode(left, right, op.asInstanceOf[Comparator])
        }
    }
  }
}


// Structures
case class FilterNode(name: StringLiteralNode, param1: ParamNode, param2: Option[ParamNode], body: BooleanExpNode, where: Option[WhereNode]) extends ASTNode {
  def this(name: StringLiteralNode, param: ParamNode, body: BooleanExpNode, where: Option[WhereNode]) = this(name, param, None, body, where)
}
/*
  'Filter' <String> '(' <ParamNode> [ ',' <ParamNode>] ')' '{' <BooleanExpNode> '}' [WhereNode]
 */
private object FilterNode {
  def visit(tokens:ListBuffer[Symbol]): FilterNode = {
    popSymbol(tokens, FILTER)
    val name = new StringLiteralNode(tokens.remove(0))
    val paramListStream = extractBracket(tokens)
    val param1 = new ParamNode(new StringLiteralNode(paramListStream(0)))
    val param2 = if(paramListStream.size == 3){
      val p = new ParamNode(new StringLiteralNode(paramListStream(2)))
      DSLParser.params = Some(Map((param1.name.string -> param1), (p.name.string -> p)))
      Some(p)
    } else {
      DSLParser.params = Some(Map((param1.name.string -> param1)))
      None
    }

    val bodyTokens = extractBrace(tokens)
    val body = BooleanExpNode.visit(bodyTokens)
    if(!bodyTokens.isEmpty){
      throw new ParserException("Malformed filter body: " + bodyTokens)
    }

    val where: Option[WhereNode] = if(!tokens.isEmpty && tokens(0).equals(WHERE)){
      Some(WhereNode.visit(tokens))
    }else{
      None
    }
    new FilterNode(name, param1, param2, body, where)
  }
}


case class ParamNode(name: StringLiteralNode) extends ASTNode
/*
  <String>
 */

case class BinaryOperationNode(param1: BooleanExpNode, param2: BooleanExpNode, binOp: BinOp) extends BooleanExpNode
/*
  <BooleanExpNode> <BinOp> <BooleanExpNode>
 */

case class ComparisonNode(param1:Value, param2:Value, comparator: Comparator) extends  BooleanExpNode
/*
  <Value> <Comparator> <Value>
 */

case class NegNode(expr: BooleanExpNode) extends BooleanExpNode
/*
  '!' <BooleanExpNode>
 */

case class ConditionalNode(branches: Seq[BranchNode], defaultBranch: BooleanExpNode) extends BooleanExpNode
/*
  'If' <BranchNode> ( 'Elif' <BranchNode>)* 'Else' '{' <BooleanExpNode> '}'
 */
private object ConditionalNode{
  def visit(tokens:ListBuffer[Symbol]): ConditionalNode = {
    popSymbol(tokens, IF)

    val branches = ListBuffer[BranchNode]()
    var loop = true

    while(loop){
      branches += BranchNode.visit(tokens)
      if(tokens(0).equals(ELSE)){
        loop = false
      }else{
        popSymbol(tokens, ELIF)
      }
    }
    val defaultBranch = visitElse(tokens)

    new ConditionalNode(branches, defaultBranch)
  }

  private def visitElse(tokens:ListBuffer[Symbol]): BooleanExpNode = {
    popSymbol(tokens, ELSE)
    val bodyTokens = extractBrace(tokens)
    val body = BooleanExpNode.visit(bodyTokens)
    if(!bodyTokens.isEmpty){
      throw new ParserException("Malformed Conditional Body: Trailing \""+bodyTokens+"\"")
    }
    body
  }
}



case class BranchNode(condition: BooleanExpNode, body: BooleanExpNode) extends ASTNode
/*
  '(' <BooleanExpNode> ')' '{' <BooleanExpNode> '}'
 */

private object BranchNode {
  def visit(tokens: ListBuffer[Symbol]): BranchNode = {
    val conditionTokens = extractBracket(tokens)

    val condition = BooleanExpNode.visit(conditionTokens)
    if(!conditionTokens.isEmpty){
      throw new ParserException("Malformed Conditional Case: Trailing \""+conditionTokens+"\"")
    }

    val bodyTokens = extractBrace(tokens)
    val body = BooleanExpNode.visit(bodyTokens)
    if(!bodyTokens.isEmpty){
      throw new ParserException("Malformed Conditional Body: Trailing \""+bodyTokens+"\"")
    }
    new BranchNode(condition, body)
  }
}

case class ReferenceNode(variable: ParamNode, parts: Seq[StringLiteralNode]) extends Value
/*
  <ParamNode> ( '.' <String>)+
 */

case class WhereNode(condition: Seq[BooleanExpNode]) extends ASTNode
/*
  'Where' '(' <BooleanExpNode> ( ',' <BooleanExpNode> )* ')'
 */
private object WhereNode{
  def visit(tokens:ListBuffer[Symbol]): WhereNode = {
    popSymbol(tokens, WHERE)
    val whereCond = extractBracket(tokens)
    val condition = ListBuffer[BooleanExpNode]()
    var i = 0
    var loop = true
    while (!whereCond.isEmpty){
      condition += BooleanExpNode.visit(whereCond)
      if (!whereCond.isEmpty){
        popSymbol(whereCond, COMMA)
      }else{
        loop = false
      }
      i+=1
      if(i > DSLParser.WHERE_MAX_EXP){
        throw new ParserException("Where condition contains > "+DSLParser.WHERE_MAX_EXP+" expressions")
      }
    }
    new WhereNode(condition)
  }
}

// Literals
case class IntegerLiteralNode(int: Int) extends Value
case class BooleanLiteralNode(bool: Boolean) extends BooleanExpNode
case class FloatLiteralNode(float: Float) extends Value
case class StringLiteralNode(string: String) extends Value{
  def this(str: UNCAPWORD) = this(str.str)
  def this(str: CAPWORD) = this(str.str)
  def this(sym: Symbol) = this(
    sym match {
      case UNCAPWORD(s) => s
      case CAPWORD(s) => s
      case _ => throw new ParserException("Cannot convert \"" + sym + "\" to StringLiteral")
    }
  )
}
case class CharLiteralNode(char: Char) extends Value


object Comparator extends Enumeration{
  type Comparator = Value
  val Eq, Ne,Gt,Lt,Ge,Le = Value
}

object BinOp extends Enumeration{
  type BinOp = Value
  val Or, And = Value
}