import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.annotation.tailrec

class Q0001_0010Spec extends AnyFlatSpec with Matchers {

  it should "Q0001" in {
    /**
     * If we list all the natural numbers below 10 that are multiples of 3 or 5, we get 3, 5, 6 and 9.
     * The sum of these multiples is 23.
     *
     * Find the sum of all the multiples of 3 or 5 below 1000.
     */
    def sum3or5(n: Int) = {
      (1 to n - 1).foldLeft(0) {
        (acc, i) => if (i % 3 == 0 || i % 5 == 0) acc + i else acc
      }
    }

    sum3or5(10) shouldBe 23
    sum3or5(1000) shouldBe 233168
  }

  it should "Q0002" in {
    /**
     * Each new term in the Fibonacci sequence is generated by adding the previous two terms.
     * By starting with 1 and 2, the first 10 terms will be:
     *
     * 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, ...
     *
     * By considering the terms in the Fibonacci sequence whose values do not exceed four million,
     * find the sum of the even-valued terms.
     */
    import Fibo._
    fibo(3) shouldBe 3
    fibo(4) shouldBe 5
    fibo(10) shouldBe 89

    fibs().take(10) shouldBe LazyList[BigInt](0, 1, 1, 2, 3, 5, 8, 13, 21, 34)
    val s = fibs().takeWhile(_ < 4000000).filter(_ % 2 == 0).sum
    s shouldBe 4613732
  }

  it should "Q0003" in {
    /**
     * The prime factors of 13195 are 5, 7, 13 and 29.
     *
     * What is the largest prime factor of the number 600851475143 ?
     */
    import Prime._
    primes.takeWhile(_ < 20) shouldBe LazyList(2, 3, 5, 7, 11, 13, 17, 19)

    var n = 600851475143L
    var i = 1
    var pMax = 2
    while (n != 1) {
      val p = primes.take(i).last
      if (n % p == 0) {
        n = n/p
        pMax = p
      }
      i = i+1
    }

    pMax shouldBe 6857
  }
}
