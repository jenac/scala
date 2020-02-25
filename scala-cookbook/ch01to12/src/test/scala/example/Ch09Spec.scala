package example

import example.otherscope.Foo
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Ch09Spec extends AnyFlatSpec with Matchers {
  "9.03" should "partially applied function" in {
    import scala.math._

    val powerOfE = pow(E, _)

    val squareOfE = powerOfE(2)

    squareOfE shouldBe pow(E, 2)
  }

  "9.05" should "closure" in {
    var hello = "Hello"

    def sayHello(name: String): String = s"$hello, $name"

    val foo = new Foo
    foo.exec(sayHello, "AI") shouldBe "Hello, AI"

    //captured variables?
    hello = "Hola"
    foo.exec(sayHello, "AI") shouldBe "Hola, AI"
  }

  "9.08" should "partial function" in {
    val divide = new PartialFunction[Int, Int] {
      override def isDefinedAt(x: Int): Boolean = x != 0

      override def apply(x: Int): Int = 42 / x
    }

    //divide takes an partial Int (without 0)
    val a = if (divide.isDefinedAt(0)) divide(0) else -1
    val b = if (divide.isDefinedAt(2)) divide(2) else -1

    a shouldBe -1
    b shouldBe 21

    val divide2: PartialFunction[Int, Int] = {
      case d: Int if d != 0 => 42 / d
    }
    //    the following throw exception
    //    0 (of class java.lang.Integer)
    //    scala.MatchError: 0 (of class java.lang.Integer)
    //    divide2(0)
    divide2.isDefinedAt(0) shouldBe false
    divide2.isDefinedAt(100) shouldBe true

    //collect will test isDefinedAt
    List(0, 1, 2) collect {
      divide2
    } shouldBe List(42, 21)
  }

  "9.08" should "partial function orElse, andElse" in {
    val convert1to5 = new PartialFunction[Int, String] {
      val nums = Array("one", "two", "three", "four", "five")

      override def isDefinedAt(x: Int): Boolean = x > 0 && x < 6

      override def apply(v1: Int): String = nums(v1 - 1)
    }

    val convert6to10 = new PartialFunction[Int, String] {
      val nums = Array("six", "seven", "eight", "nine", "ten")

      override def isDefinedAt(x: Int): Boolean = x > 5 && x < 11

      override def apply(v1: Int): String = nums(v1 - 6)
    }

    val toString1to10 = convert1to5 orElse convert6to10
    toString1to10(3) shouldBe "three"
    toString1to10(7) shouldBe "seven"

    List(4, 6, 18) collect {
      convert1to5 orElse convert6to10
    } shouldBe List("four", "six")
  }

}
