package services.parser.dsl

object DSLLexer {

  // Literals
  val CAPWORD_REG = raw"([A-Z][A-Za-z0-9]*)(.*)".r
  val UNCAPWORD_REG = raw"([a-z][A-Za-z0-9]*)(.*)".r
  val INTLITERAL_REG = raw"([+-]?\\d+)(.*)".r
  val BOOLEANLITERAL_REG = raw"(?i)(TRUE|FALSE)(.*)".r
  val FLOATLITERAL_REG = raw"(([+-]?(\\d+\\.)?\\d+))(.*)".r
  val CHARLITERAL_REG = raw"('.')(.*)".r


  // Keywords
  val DEF_REG = raw"(?i)(FILTER)".r
  val IF_REG = raw"(?i)(IF)".r
  val ELIF_REG = raw"(?i)(ELIF)".r
  val ELSE_REG = raw"(?i)(ELSE)".r
  val WHERE_REG = raw"(?i)(WHERE)".r

  // Punctuation
  val OPENBRACKET_REG = raw"[(](.*)".r
  val CLOSEBRACKET_REG = raw"[)](.*)".r
  val COMMA_REG = raw"[,](.*)".r
  val OPENBRACE_REG = raw"[{](.*)".r
  val CLOSEBRACE_REG = raw"[}](.*)".r
  val DOT_REG = raw"[.](.*)".r

  // Boolean Logic
  val NOT_REG = raw"!(.*)".r
  val AND_REG = raw"[&]{2}(.*)".r
  val OR_REG = raw"[|]{2}(.*)".r


  // Comparators
  val EQ_REG = raw"==(.*)".r
  val NE_REG = raw"!=(.*)".r
  val GT_REG = raw">(.*)".r
  val GE_REG = raw">=(.*)".r
  val LT_REG = raw"<(.*)".r
  val LE_REG = raw"<=(.*)".r


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
      case _ => throw new IllegalArgumentException("Invalid token: " + token)
    }
  }
}


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