import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TailRecursiveFunctionsSpec extends AnyFlatSpec with Matchers {

  "factorial" should "works" in {
    TailRecursiveFunctions.factorial(1) shouldBe 1
    TailRecursiveFunctions.factorial(2) shouldBe 2
    TailRecursiveFunctions.factorial(3) shouldBe 6
    TailRecursiveFunctions.factorial(4) shouldBe 24
    TailRecursiveFunctions.factorial(5) shouldBe 120
  }

  "fibo" should "works" in {
    0 shouldBe TailRecursiveFunctions.fibo(0)
    1 shouldBe TailRecursiveFunctions.fibo(1)
    1 shouldBe TailRecursiveFunctions.fibo(2)
    2 shouldBe TailRecursiveFunctions.fibo(3)
    3 shouldBe TailRecursiveFunctions.fibo(4)
    5 shouldBe TailRecursiveFunctions.fibo(5)
    8 shouldBe TailRecursiveFunctions.fibo(6)
    13 shouldBe TailRecursiveFunctions.fibo(7)
    21 shouldBe TailRecursiveFunctions.fibo(8)
    34 shouldBe TailRecursiveFunctions.fibo(9)
    55 shouldBe TailRecursiveFunctions.fibo(10)
  }

  "fib" should "works" in {
    0 shouldBe TailRecursiveFunctions.fib(0)
    1 shouldBe TailRecursiveFunctions.fib(1)
    1 shouldBe TailRecursiveFunctions.fib(2)
    2 shouldBe TailRecursiveFunctions.fib(3)
    3 shouldBe TailRecursiveFunctions.fib(4)
    5 shouldBe TailRecursiveFunctions.fib(5)
    8 shouldBe TailRecursiveFunctions.fib(6)
    13 shouldBe TailRecursiveFunctions.fib(7)
    21 shouldBe TailRecursiveFunctions.fib(8)
    34 shouldBe TailRecursiveFunctions.fib(9)
    55 shouldBe TailRecursiveFunctions.fib(10)
  }

  "finfFirst" should "works" in {
    val array = Array("a", "b", "c")

    def funcA(p: String): Boolean = p.toUpperCase == "C"

    val funcB: String => Boolean = p => p.toUpperCase == "A"
    val funcN: String => Boolean = p => p.toUpperCase == "N"

    TailRecursiveFunctions.findFirst(array, funcA) shouldBe 2
    TailRecursiveFunctions.findFirst(array, funcB) shouldBe 0
    TailRecursiveFunctions.findFirst(array, funcN) shouldBe -1
  }

  "isSorted" should "works" in {
    TailRecursiveFunctions.isSorted(Array(1, 3, 5, 7), (x: Int, y: Int) => x < y) shouldBe true

    TailRecursiveFunctions.isSorted(Array(7, 5, 1, 3), (x: Int, y: Int) => x > y) shouldBe false

    TailRecursiveFunctions.isSorted(Array("Scala", "Exercises"), (x: String, y: String) => x.length < y.length) shouldBe true
  }
}
