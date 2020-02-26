import scala.annotation.tailrec

object CommonFunctions {
  def factorial(n: Int): Int = {
    @tailrec
    def go(n: Int, acc: Int): Int =
      if (n <= 0) acc else go(n - 1, n * acc)

    go(n, 1)
  }

  def fibo(n: Int): Int = {
    @tailrec
    def go(n: Int, a: Int, b: Int): Int = n match {
      case 0 => a
      case _ => go(n - 1, b, a + b)
    }

    go(n, 0, 1)
  }

  def findFirst[A](as: Array[A], p: A => Boolean): Int = { //p is a function
    @tailrec
    def loop(n: Int): Int = if (n >= as.length) -1
    else if (p(as(n))) n
    else loop(n + 1)

    loop(0)
  }
}
