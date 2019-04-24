package services

object Utils {
  def looseCompare(s1: String, s2: String) = s1.toUpperCase.replace(" ", "_") == s2.toUpperCase.replace(" ", "_")

  def toSnake(s: String): String = s.trim.toUpperCase.replace(" ", "_")
}