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

  "5.3" should "implement takeWhile" in {
    def takeWhile[A](input: LazyList[A])(p: A => Boolean): LazyList[A] = input match {
      case h #:: t if p(h) => h #:: takeWhile(t)(p)
      case h #:: t => takeWhile(t)(p)
      case _ => LazyList.empty
    }

    takeWhile(LazyList(1, 2, 3))(_ % 2 == 0) shouldBe LazyList(2)
    takeWhile(LazyList(1, 2, 3, 4, 5))(_ < 3).toList shouldBe LazyList(1, 2)
    takeWhile(LazyList(1, 2, 3, 4, 5))(_ < 0).toList shouldBe LazyList.empty
  }

  "5.4" should "implement forAll" in {
    def forAll[A](input: LazyList[A])(p: A => Boolean): Boolean = input match {
      case h #:: t if p(h) => forAll(t)(p)
      case _ #:: _ => false
      case _ => true
    }

    forAll(LazyList(1, 2, 3))(_ % 2 == 0) shouldBe false
    forAll(LazyList("a", "b", "c"))(_.size > 0) shouldBe true

    def forAll2[A](s: LazyList[A], f: A => Boolean): Boolean =
      s.foldRight(true)((a, b) => f(a) && b)

    forAll2(LazyList(1, 2, 3), (x: Int) => x % 2 == 0) shouldBe false
    forAll2(LazyList("a", "b", "c"), (x: String) => x.size > 0) shouldBe true
  }

  "5.5" should "implement takeWhile using foldRight" in {
    def takeWhile[A](input: LazyList[A])(p: A => Boolean): LazyList[A] =
      input.foldRight(LazyList[A]())((a, b) =>
        if (p(a)) a #:: b
        else LazyList.empty)

    takeWhile(LazyList(1, 2, 3, 4, 5))(_ < 3).toList shouldBe LazyList(1, 2)
    takeWhile(LazyList(1, 2, 3, 4, 5))(_ < 0).toList shouldBe LazyList.empty
  }

  "5.6" should "implement headOption with foldRight" in {
    def headOption[A](input: LazyList[A]): Option[A] =
      input.foldRight(None: Option[A])((h, _) => Some(h))

    headOption(LazyList(1, 2, 3)) shouldBe Some(1)
    headOption(LazyList.empty) shouldBe None
  }
}
