import scala.annotation.tailrec

object Fibo {

  /**
   * Fibo start from 1,
   * 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, ...
   *
   * @param n the nth fibo
   * @return value of nth fibo
   */
  def fibo(n: Int): Int = {
    @tailrec
    def loop(n: Int, prev: Int, cur: Int): Int =
      if (n <= 1) prev
      else loop(n - 1, cur, prev + cur)

    loop(n, 1, 2)
  }

  /**
   * A Fibo list start from 0
   * fibs().take(10) shouldBe LazyList[BigInt](0, 1, 1, 2, 3, 5, 8, 13, 21, 34)
   *
   * @param x - 1st element
   * @param y - 2nd element
   * @return LazyList contains fibo elements
   */
  def fibs(x: BigInt = 0, y: BigInt = 1): LazyList[BigInt] = x #:: fibs(y, x + y)
}
