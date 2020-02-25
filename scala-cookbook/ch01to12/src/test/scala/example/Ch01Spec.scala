package example

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Ch01Spec extends AnyFlatSpec with Matchers {
  it should "1.10" in {
    import example.utils.StringUtils._
    val result = "HAL".increment
    result shouldEqual "IBM"

    "4".plusOne shouldBe 5
    "0".asBoolean shouldBe false
    "1".asBoolean shouldBe true
  }
}
