import PureFunctionState._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
class PureFunctionStateSpec extends AnyFlatSpec with Matchers {
  "6.1" should "generate non negative rand" in {
    val rng1 = SimpleRNG(47)
    val (result1, rng11) = nonNegativeInt(rng1)
    val result2 = nonNegativeInt(rng11)._1

    result1 should be >= 0
    result2 should be >= 0
    result1 should not be result2
  }

  "6.2" should "generate double between [0, 1)" in {
    val rng2 = SimpleRNG(47)
    val (double1, rng21) = double(rng2)
    val double2 = double(rng21)._1

    double1.toInt should be >= 0
    double2.toInt should be >= 0
    double1 should not be double2
  }

  "6.4" should "generate list of random int" in {
    val (list1, rng3) = ints(5)(SimpleRNG(47))
    val list2 = ints(5)(rng3)._1
    list1.size shouldBe 5
    list1.headOption should not be list2
  }
}
