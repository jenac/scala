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
}
