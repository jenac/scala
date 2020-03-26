object Math {

  //greatest common divisor
  def gcd(a: Int, b: Int): Int = a % b match {
    case 0 => b
    case c => gcd(b, c)
  }

  //least common multiple
  def lcm(a: Int, b: Int): Int = a * b / gcd(a, b)

  //def gcd(a: Int, b: Int): Int = a % b match {
  //      case 0 => b
  //      case c => gcd(b, c)
  //    }
}
