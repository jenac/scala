object Prime {
  /**
   * a list contains prime numbers
   * primes.takeWhile(_ < 20) shouldBe LazyList(2, 3, 5, 7, 11, 13, 17, 19)
   */
  val primes: LazyList[Int] = 2 #:: LazyList.from(3, 2).filter(isPrime)

  /**
   * Check if n is prime number
   * @param n - the number
   * @return true/false
   */
  def isPrime(n: Int): Boolean = primes.takeWhile(p => p * p <= n).forall(n % _ != 0)
}
