import scala.annotation.tailrec

object TailRecursiveFunctions {
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

  def fib(n: Int): Int = {
    @tailrec
    def loop(n: Int, prev: Int, cur: Int): Int =
      if (n <= 0) prev
      else loop(n - 1, cur, prev + cur)

    loop(n, 0, 1)
  }

  def findFirst[A](as: Array[A], p: A => Boolean): Int = { //p is a function
    @tailrec
    def loop(n: Int): Int =
      if (n >= as.length) -1
      else if (p(as(n))) n
      else loop(n + 1)

    loop(0)
  }

  def isSorted[A](as: Array[A], ordered: (A, A) => Boolean): Boolean = {
    @tailrec
    def loop(n: Int): Boolean =
      if (n >= as.length - 1) true
      else if (!ordered(as(n), as(n + 1))) false
      else loop(n + 1)

    loop(0)
  }
}
