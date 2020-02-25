package example.utils

object ValueUtils {
  implicit class StringToInt(s: String) {
    def toInt(radix: Int) = Integer.parseInt(s, radix)
  }
}
