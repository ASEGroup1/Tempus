package services.parser.dsl

object DSLLexer {

  sealed trait Symbol

  // Literals
  case class CAPWORD(str: String) extends Symbol
  val CAPWORDREG = raw"([A-Z][A-Za-z]*)".r
  case class UNCAPWORD(str: String) extends Symbol
  val UNCAPWORDREG = raw"([a-z][A-Za-z]*)".r
  case class INTLITERAL(int: Int) extends Symbol
  val INTLITERALREG = raw"([+-]?\\d+)".r
  case class BOOLEANLITERAL(bool: Boolean) extends  Symbol
  val BOOLEANLITERALREG = raw"([(TRUE)(FALSE)])".r
  case class FLOATLITERAL(float:Float) extends Symbol
  val FLOATLITERALREG = raw"(([+-]?(\\d+\\.)?\\d+))".r
  case class CHARLITERAL(char:Char) extends Symbol
  val CHARLITERALREG = raw"('.')".r


  // Keywords
  case object DEF extends Symbol
  val DEFREG = raw"(?i)(DEF)".r
  case object IF extends Symbol
  val IFREG = raw"(?i)(IF)".r
  case object ELIF extends Symbol
  val ELIFREG = raw"(?i)(ELIF)".r
  case object ELSE extends  Symbol
  val ELSEREG = raw"(?i)(ELSE)".r
  case object WHERE extends  Symbol
  val WHEREREG = raw"(?i)(WHERE)".r

  // Punctuation
  case object OPENBRACKET extends Symbol
  val OPENBRACKETREG = raw"[(](.*)".r
  case object CLOSEBRACKET extends  Symbol
  val CLOSEBRACKETREG = raw"[)](.*)".r
  case object COMMA extends Symbol
  val COMMAREG = raw"[,](.*)".r
  case object OPENBRACE extends Symbol
  val OPENBRACEREG = raw"[{](.*)".r
  case object CLOSEBRACE extends Symbol
  val CLOSEBRACEREG = raw"[}](.*)".r
  case object STOP extends Symbol
  val STOPREG = raw"[.](.*)".r

  // Boolean Logic
  case object NOT extends Symbol
  val NOTREG = raw"!(.*)".r
  case object AND extends Symbol
  val ANDREG = raw"[&]{2}(.*)".r
  case object OR extends Symbol
  val ORREG = raw"[|]{2}(.*)".r


  // Comparators
  case object EQ extends Symbol
  val EQREG = raw"==(.*)".r
  case object NE extends Symbol
  val NEREG = raw"!=(.*)".r
  case object GT extends Symbol
  val GTREG = raw">(.*)".r
  case object GE extends Symbol
  val GEREG = raw">=(.*)".r
  case object LT extends Symbol
  val LTREG = raw"<(.*)".r
  case object LE extends Symbol
  val LEREG = raw"<=(.*)".r




  def lex(dsl: String): Seq[Symbol] = dsl.split("\\s+").flatMap(processToken(_))


  def processToken(token: String): Seq[Symbol] = {
    token match{
      // Comparators
      case EQREG(c) => Array(EQ) ++ processToken(c)
      case NEREG(c) => Array(NE) ++ processToken(c)
      case GEREG(c) => Array(GE) ++ processToken(c)
      case GTREG(c) => Array(GT) ++ processToken(c)
      case LEREG(c) => Array(LE) ++ processToken(c)
      case LTREG(c) => Array(LT) ++ processToken(c)

      //Boolean logic
      case NOTREG(c) => Array(NOT) ++ processToken(c)
      case ANDREG(c) => Array(AND) ++ processToken(c)
      case ORREG(c) => Array(OR) ++ processToken(c)

      // Punctuation
      case OPENBRACKETREG(c) => Array(OPENBRACKET) ++ processToken(c)
      case CLOSEBRACKETREG(c) => Array(CLOSEBRACKET) ++ processToken(c)
      case COMMAREG(c) => Array(COMMA) ++ processToken(c)
      case OPENBRACEREG(c) => Array(OPENBRACE) ++ processToken(c)
      case CLOSEBRACEREG(c) => Array(CLOSEBRACE) ++ processToken(c)
      case STOPREG(c) => Array(STOP) ++ processToken(c)

      // Keywords
      case DEFREG(_) => Array(DEF)
      case IFREG(_) => Array(IF)
      case ELIFREG(_) => Array(ELIF)
      case ELSEREG(_) => Array(ELSE)
      case WHEREREG(_) => Array(WHERE)

      // Literals
      case CAPWORDREG(v) => Array(CAPWORD(v))
      case UNCAPWORDREG(v) => Array(UNCAPWORD(v))
      case INTLITERALREG(v) => Array(INTLITERAL(v.toInt))
      case BOOLEANLITERALREG(v) => Array(BOOLEANLITERAL(v.toBoolean))
      case FLOATLITERALREG(v) => Array(FLOATLITERAL(v.toFloat))
      case CHARLITERALREG(v) => Array(CHARLITERAL(v.charAt(1)))
      case "\\s*" => Array[Symbol]()
      case _ => throw new IllegalArgumentException
    }
  }


}
