package services.parser.dsl

object DSLLexer {

  // Literals
  private val CAPWORD_REG = raw"([A-Z][A-Za-z0-9]*)(.*)".r
  private val UNCAPWORD_REG = raw"([a-z][A-Za-z0-9]*)(.*)".r
  private val INTLITERAL_REG = raw"([+-]?\\d+)(.*)".r
  private val BOOLEANLITERAL_REG = raw"(?i)(TRUE|FALSE)(.*)".r
  private val FLOATLITERAL_REG = raw"(([+-]?(\\d+\\.)?\\d+))(.*)".r
  private val CHARLITERAL_REG = raw"('.')(.*)".r

  // Keywords
  private val DEF_REG = raw"(?i)(FILTER)".r
  private val IF_REG = raw"(?i)(IF)".r
  private val ELIF_REG = raw"(?i)(ELIF)".r
  private val ELSE_REG = raw"(?i)(ELSE)".r
  private val WHERE_REG = raw"(?i)(WHERE)".r

  // Punctuation
  private val OPENBRACKET_REG = raw"[(](.*)".r
  private val CLOSEBRACKET_REG = raw"[)](.*)".r
  private val COMMA_REG = raw"[,](.*)".r
  private val OPENBRACE_REG = raw"[{](.*)".r
  private val CLOSEBRACE_REG = raw"[}](.*)".r
  private val DOT_REG = raw"[.](.*)".r

  // Boolean Logic
  private val NOT_REG = raw"!(.*)".r
  private val AND_REG = raw"[&]{2}(.*)".r
  private val OR_REG = raw"[|]{2}(.*)".r

  // Comparators
  private val EQ_REG = raw"==(.*)".r
  private val NE_REG = raw"!=(.*)".r
  private val GT_REG = raw">(.*)".r
  private val GE_REG = raw">=(.*)".r
  private val LT_REG = raw"<(.*)".r
  private val LE_REG = raw"<=(.*)".r


  def lex(dsl: String): Seq[Symbol] = dsl.split("\\s+").flatMap(processToken(_))

  private def processToken(token: String): Seq[Symbol] = {
    token match{
      // Comparators
      case EQ_REG(c) => Array(EQ) ++ processToken(c)
      case NE_REG(c) => Array(NE) ++ processToken(c)
      case GE_REG(c) => Array(GE) ++ processToken(c)
      case GT_REG(c) => Array(GT) ++ processToken(c)
      case LE_REG(c) => Array(LE) ++ processToken(c)
      case LT_REG(c) => Array(LT) ++ processToken(c)

      //Boolean logic
      case NOT_REG(c) => Array(NOT) ++ processToken(c)
      case AND_REG(c) => Array(AND) ++ processToken(c)
      case OR_REG(c) => Array(OR) ++ processToken(c)

      // Punctuation
      case OPENBRACKET_REG(c) => Array(OPENBRACKET) ++ processToken(c)
      case CLOSEBRACKET_REG(c) => Array(CLOSEBRACKET) ++ processToken(c)
      case COMMA_REG(c) => Array(COMMA) ++ processToken(c)
      case OPENBRACE_REG(c) => Array(OPENBRACE) ++ processToken(c)
      case CLOSEBRACE_REG(c) => Array(CLOSEBRACE) ++ processToken(c)
      case DOT_REG(c) => Array(DOT) ++ processToken(c)

      // Keywords
      case DEF_REG(_) => Array(FILTER)
      case IF_REG(_) => Array(IF)
      case ELIF_REG(_) => Array(ELIF)
      case ELSE_REG(_) => Array(ELSE)
      case WHERE_REG(_) => Array(WHERE)

      // Literals
      case BOOLEANLITERAL_REG(v, c) => Array(new BOOLEANLITERAL(v)) ++ processToken(c)
      case CAPWORD_REG(v, c) => Array(CAPWORD(v)) ++ processToken(c)
      case UNCAPWORD_REG(v, c) => Array(UNCAPWORD(v)) ++ processToken(c)
      case FLOATLITERAL_REG(v, c) => Array(new FLOATLITERAL(v))++ processToken(c)
      case INTLITERAL_REG(v, c) => Array(new INTLITERAL(v))++ processToken(c)
      case CHARLITERAL_REG(v, c) => Array(new CHARLITERAL(v))++ processToken(c)

      // Misc
      case "\\s*" => Array[Symbol]()
      case "" => Array[Symbol]()
      case _ => throw new IllegalArgumentException(s"""Invalid token: "$token"""")
    }
  }
}


// Symbol Instances
sealed trait Symbol

// Literals
case class CAPWORD(str: String) extends Symbol
case class UNCAPWORD(str: String) extends Symbol
case class INTLITERAL(int: Int) extends Symbol{
  def this(str: String) = this(str.toInt)
}
case class BOOLEANLITERAL(bool: Boolean) extends Symbol {
  def this(str: String) = this(str.toBoolean)
}
case class FLOATLITERAL(float:Float) extends Symbol{
  def this(str: String) = this(str.toFloat)
}
case class CHARLITERAL(char:Char) extends Symbol{
  def this(str: String) = this(str.charAt(1))
}

// Keywords
case object FILTER extends Symbol
case object IF extends Symbol
case object ELIF extends Symbol
case object ELSE extends  Symbol
case object WHERE extends  Symbol

// Punctuation
case object OPENBRACKET extends Symbol
case object CLOSEBRACKET extends  Symbol
case object COMMA extends Symbol
case object OPENBRACE extends Symbol
case object CLOSEBRACE extends Symbol
case object DOT extends Symbol

// Boolean Logic
case object NOT extends Symbol
case object AND extends Symbol
case object OR extends Symbol

// Comparators
case object EQ extends Symbol
case object NE extends Symbol
case object GT extends Symbol
case object GE extends Symbol
case object LT extends Symbol
case object LE extends Symbol