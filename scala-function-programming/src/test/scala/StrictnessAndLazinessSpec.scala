import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class StrictnessAndLazinessSpec extends AnyFlatSpec with Matchers {

  "5.1" should "implement toList" in {
    def toList[A](s: LazyList[A]): List[A] = s match { //Stream is obsoleted
      case h #:: t => h :: toList(t)
      case _ => List()
    }

    val s = LazyList(1, 2, 3)
    toList(s) shouldBe List(1, 2, 3)
  }

  "5.2" should "implement take and drop" in {
    def take[A](s: LazyList[A], n: Int): LazyList[A] = s match {
      case h #:: t if n > 0 => h #:: t.take(n - 1)
      case h #:: _ if n == 0 => h #:: LazyList.empty
      case _ => LazyList.empty
    }

    take(LazyList(1, 2, 3), 2).toList shouldBe List(1, 2)

    def drop[A](s: LazyList[A], n: Int): LazyList[A] = s match {
      case _ #:: t if n > 0 => t.drop(n - 1)
      case _ => s
    }

    drop(LazyList(1, 2, 3, 4, 5), 2).toList shouldBe List(3, 4, 5)
  }
}
