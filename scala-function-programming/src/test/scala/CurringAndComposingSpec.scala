import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CurringAndComposingSpec extends AnyFlatSpec with Matchers {

  "curring" should "works" in {
    def curry[A, B, C](f: (A, B) => C): A => (B => C) = a => b => f(a, b)

    def f(a: Int, b: Int): Int = a + b

    def g(a: Int)(b: Int): Int = a + b

    curry(f)(1)(1) == f(1, 1) shouldBe true
    curry(f)(1)(1) == g(1)(1) shouldBe true
  }

  "uncurring" should "works" in {
    def uncurry[A, B, C](f: A => B => C): (A, B) => C = (a, b) => f(a)(b)

    def f(a: Int, b: Int): Int = a + b

    def g(a: Int)(b: Int): Int = a + b

    uncurry(g)(1, 1) == g(1)(1) shouldBe true
    uncurry(g)(1, 1) == f(1, 1) shouldBe true
  }

  "composing" should "works" in {
    def compose[A, B, C](f: B=> C, g: A => B): A=>C = a => f(g(a))

    def f(b: Int): Int = b/2
    def g(a: Int): Int = a+2

    compose(f, g)(0) == compose(g, f)(0) shouldBe false
    compose(f, g)(2) shouldBe 2
    compose(g, f)(2) shouldBe 3
  }
}
