package services

object Utils {
  def looseCompare(s1: String, s2: String) = s1.toLowerCase.replace(" ", "") == s2.toLowerCase.replace(" ", "")
  def reduceSpecificity(s: String) = s.toLowerCase.replace(" ", "")
}
