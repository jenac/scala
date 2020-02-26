import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Q0001_0010Spec extends AnyFlatSpec with Matchers {

  it should "Q0001" in {
    /**
     * If we list all the natural numbers below 10 that are multiples of 3 or 5, we get 3, 5, 6 and 9.
     * The sum of these multiples is 23.
     *
     * Find the sum of all the multiples of 3 or 5 below 1000.
     */
    def sum3or5(n: Int) = {
      (1 to n-1).foldLeft(0) {
        (acc, i) => if (i % 3 == 0 || i % 5 == 0) acc + i else acc
      }
    }

    sum3or5(10) shouldBe 23
    sum3or5(1000) shouldBe 233168
  }
}
