import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MiscSpec extends AnyFlatSpec with Matchers {
  it should "foldLeft vs foldRight" in {
    val values = List(1, 2, 3, 4, 5)
    val left = values.foldLeft(List.empty[Int])((acc, e) => e :: acc)
    val right = values.foldRight(List.empty[Int])((e, acc) => e :: acc)
    left shouldBe List(5, 4, 3, 2, 1)
    right shouldBe List(1, 2, 3, 4, 5)
  }

  it should "collect all left values" in {
    val values = List(Left("l-1"), Right("r-1"), Left("l-2"))
    val r1 = values.filter(_.isLeft).map(_.left.getOrElse(None))
    val r2 = values.map(_.left.getOrElse(None))
    val r3 = values.collect(_.left.getOrElse(None))
    r1 shouldBe List("l-1", "l-2")
    r2 shouldBe List("l-1", None, "l-2")
    r3 shouldBe List("l-1", None, "l-2")

    val lefts = values.foldRight(List.empty[String])((i, acc) => i match {
      case Left(value) => value :: acc
      case Right(_) => acc
    })
    lefts shouldBe List("l-1", "l-2")

    val lefts2 = values.collect { case Left(v) => v}
    lefts2 shouldBe List("l-1", "l-2")
  }
}
