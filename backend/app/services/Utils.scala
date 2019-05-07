package services

object Utils {
  def looseCompare(s1: String, s2: String) = toSnake(s1) == toSnake(s2)

  def toSnake(s: String): String = s.trim.toUpperCase.replace(" ", "_")

  def toNatLang(s: String) = s.toLowerCase().split("_").filter(_.length > 0).map(w => w.charAt(0).toUpper + w.substring(1, w.length)).mkString(" ")
}