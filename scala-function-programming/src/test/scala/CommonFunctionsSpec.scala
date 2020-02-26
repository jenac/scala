import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CommonFunctionsSpec extends AnyFlatSpec with Matchers {

  "factorial" should "works" in {
    CommonFunctions.factorial(1) shouldBe 1
    CommonFunctions.factorial(2) shouldBe 2
    CommonFunctions.factorial(3) shouldBe 6
    CommonFunctions.factorial(4) shouldBe 24
    CommonFunctions.factorial(5) shouldBe 120
  }

  "fibo" should "works" in {
    0 shouldBe CommonFunctions.fibo(0)
    1 shouldBe CommonFunctions.fibo(1)
    1 shouldBe CommonFunctions.fibo(2)
    2 shouldBe CommonFunctions.fibo(3)
    3 shouldBe CommonFunctions.fibo(4)
    5 shouldBe CommonFunctions.fibo(5)
    8 shouldBe CommonFunctions.fibo(6)
    13 shouldBe CommonFunctions.fibo(7)
    21 shouldBe CommonFunctions.fibo(8)
    34 shouldBe CommonFunctions.fibo(9)
    55 shouldBe CommonFunctions.fibo(10)
  }

  "finfFirst" should "works" in {
    val array = Array("a", "b", "c")
    def funcA(p: String): Boolean = p.toUpperCase == "C"
    val funcB: String => Boolean = p => p.toUpperCase == "A"
    val funcN: String => Boolean = p => p.toUpperCase == "N"

    CommonFunctions.findFirst(array, funcA) shouldBe 2
    CommonFunctions.findFirst(array, funcB) shouldBe 0
    CommonFunctions.findFirst(array, funcN) shouldBe -1
  }
}
