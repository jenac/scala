package example.utils

object StringUtils {
  implicit class StringImprovements(s: String) {
    def increment = s.map(c => (c+1).toChar)
    def decrement: String = s.map(c => (c-1).toChar)
    def hideAll: String = s.replaceAll(".", "*")
    def plusOne = s.toInt + 1
    def asBoolean = s match {
      case "0" | "zero" | "" | " " => false
      case _ => true
    }
  }
}
