import java.time.LocalDate

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
      input.foldRight(LazyList.empty[A])((a, b) =>
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

  "5.7" should "implement map, filter, append, flatMap using foldRight" in {
    def map[A, B](input: LazyList[A])(f: A => B): LazyList[B] =
      input.foldRight(LazyList.empty[B])((e, acc) => f(e) #:: acc)

    map(LazyList(1, 2, 3))(_ * 2) shouldBe LazyList(2, 4, 6)

    def filter[A](input: LazyList[A])(f: A => Boolean): LazyList[A] =
      input.foldRight(LazyList.empty[A])((e, acc) => if (f(e)) e #:: acc else acc)

    filter(LazyList(1, 2, 3, 4, 5, 6))(_ <= 3) shouldBe LazyList(1, 2, 3)

    def append[A, B >: A](a: LazyList[A], b: LazyList[B]): LazyList[B] =
      a.foldRight(b)((e, acc) => e #:: acc)

    append(LazyList("a", "b", "c"), LazyList("1", "2", "3")) shouldBe LazyList("a", "b", "c", "1", "2", "3")

    def flatMap[A, B](input: LazyList[A])(f: A => LazyList[B]): LazyList[B] =
      input.foldRight(LazyList.empty[B])((h, t) => append(f(h), t))

    flatMap(LazyList(2, 3))(s => (0 to s).to(LazyList)) shouldBe LazyList(0, 1, 2, 0, 1, 2, 3)
  }

  "5.8" should "define infinity constants" in {
    def constant[A](a: A): LazyList[A] = a #:: constant(a)

    constant(12).take(3) shouldBe LazyList(12, 12, 12)
    constant(12).take(5) shouldBe LazyList(12, 12, 12, 12, 12) //also right
  }

  "5.9" should "implement infinite from n" in {
    def from(n: Int): LazyList[Int] = n #:: from(n + 1)

    from(6).take(3) shouldBe LazyList(6, 7, 8)
  }

  "5.10" should "implement infinite fib" in {
    val fibs = {
      def go(f0: Int, f1: Int): LazyList[Int] = f0 #:: go(f1, f0 + f1)

      go(0, 1)
    }

    fibs.take(3) shouldBe LazyList(0, 1, 1)
    fibs.take(7) shouldBe LazyList(0, 1, 1, 2, 3, 5, 8)
  }

  "5.11" should "implement unfold" in {
    def unfold[A, S](z: S)(f: S => Option[(A, S)]): LazyList[A] = f(z) match {
      case Some((a, s)) => a #:: unfold(s)(f)
      case None => LazyList.empty[A]
    }

    //5.12 implement fibs, from, constants and ones
    def fibsViaUnfold =
      unfold((0, 1)) { case (f0, f1) => Some((f0, (f1, f0 + f1))) }

    fibsViaUnfold.take(7).toList shouldBe List(0, 1, 1, 2, 3, 5, 8)

    def fromViaUnfold(n: Int) = unfold(n)(n => Some((n, n + 1)))

    fromViaUnfold(100).take(5) shouldBe LazyList(100, 101, 102, 103, 104)

    def constantsViaUnfold(n: Int) = unfold(n)(n => Some(n, n))

    constantsViaUnfold(12).take(3) shouldBe LazyList(12, 12, 12)

    def onesViaUnfold = unfold(1)(_ => Some((1, 1)))

    onesViaUnfold.take(5) shouldBe LazyList(1, 1, 1, 1, 1)

  }
}
