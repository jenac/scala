import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class FunctionDataStructureSpec extends AnyFlatSpec with Matchers {
  "2.6" should "match result" in {
    val x = List(1,2, 3,4,5) match {
      case x :: 2 :: 4 :: _  => x
      case Nil => 42
      case x::y::3::4::_ => x+y
      case h::t => h + 100
      case _ => 101
    }

    x shouldBe 3
  }
}
