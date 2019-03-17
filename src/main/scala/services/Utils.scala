package services

object Utils {
  def looseCompare(s1: String, s2: String) = s1.toUpperCase.replace(" ", "_") == s2.toUpperCase.replace(" ", "_")

  def toSnake(s: String) = {
    var s1 = s

    if (s(0) == 32) s1 = s.substring(1, s.length)
    if (s1(s1.length - 1) == 32) s1 = s1.substring(0, s1.length - 2)
    s1.toUpperCase.replace(" ", "_")
  }
}