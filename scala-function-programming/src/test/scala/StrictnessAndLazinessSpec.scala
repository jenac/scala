import java.time.LocalDate

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import StrictnessAndLaziness._

class StrictnessAndLazinessSpec extends AnyFlatSpec with Matchers {

  "5.1" should "implement toList" in {
    val s = LazyList(1, 2, 3)
    toList(s) shouldBe List(1, 2, 3)
  }

  "5.2" should "implement take and drop" in {
    take(LazyList(1, 2, 3), 2).toList shouldBe List(1, 2)
    drop(LazyList(1, 2, 3, 4, 5), 2).toList shouldBe List(3, 4, 5)
  }

  "5.3" should "implement takeWhile" in {
    takeWhile(LazyList(1, 2, 3))(_ % 2 == 0) shouldBe LazyList(2)
    takeWhile(LazyList(1, 2, 3, 4, 5))(_ < 3).toList shouldBe LazyList(1, 2)
    takeWhile(LazyList(1, 2, 3, 4, 5))(_ < 0).toList shouldBe LazyList.empty
  }

  "5.4" should "implement forAll" in {
    forAll(LazyList(1, 2, 3))(_ % 2 == 0) shouldBe false
    forAll(LazyList("a", "b", "c"))(_.size > 0) shouldBe true

    forAll2(LazyList(1, 2, 3), (x: Int) => x % 2 == 0) shouldBe false
    forAll2(LazyList("a", "b", "c"), (x: String) => x.size > 0) shouldBe true
  }

  "5.5" should "implement takeWhile using foldRight" in {
    takeWhileViaFoldRight(LazyList(1, 2, 3, 4, 5))(_ < 3).toList shouldBe LazyList(1, 2)
    takeWhileViaFoldRight(LazyList(1, 2, 3, 4, 5))(_ < 0).toList shouldBe LazyList.empty
  }

  "5.6" should "implement headOption with foldRight" in {
    headOption(LazyList(1, 2, 3)) shouldBe Some(1)
    headOption(LazyList.empty) shouldBe None
  }

  "5.7" should "implement map, filter, append, flatMap using foldRight" in {
    map(LazyList(1, 2, 3))(_ * 2) shouldBe LazyList(2, 4, 6)
    filter(LazyList(1, 2, 3, 4, 5, 6))(_ <= 3) shouldBe LazyList(1, 2, 3)
    append(LazyList("a", "b", "c"), LazyList("1", "2", "3")) shouldBe LazyList("a", "b", "c", "1", "2", "3")
    flatMap(LazyList(2, 3))(s => (0 to s).to(LazyList)) shouldBe LazyList(0, 1, 2, 0, 1, 2, 3)
  }

  "5.8" should "define infinity constants" in {
    constant(12).take(3) shouldBe LazyList(12, 12, 12)
    constant(12).take(5) shouldBe LazyList(12, 12, 12, 12, 12) //also right
  }

  "5.9" should "implement infinite from n" in {
    from(6).take(3) shouldBe LazyList(6, 7, 8)
  }

  "5.10" should "implement infinite fib" in {
    fibs.take(3) shouldBe LazyList(0, 1, 1)
    fibs.take(7) shouldBe LazyList(0, 1, 1, 2, 3, 5, 8)
  }

  "5.12/13" should "implement unfold, implement fibs, from, constants and ones" in {
    fibsViaUnfold.take(7).toList shouldBe List(0, 1, 1, 2, 3, 5, 8)
    fromViaUnfold(100).take(5) shouldBe LazyList(100, 101, 102, 103, 104)
    constantsViaUnfold(12).take(3) shouldBe LazyList(12, 12, 12)
    onesViaUnfold.take(5) shouldBe LazyList(1, 1, 1, 1, 1)
  }

  "5.13" should "implement map, take, takeWhile, zipWith using unfold" in {
    mapViaUnfold(LazyList(1, 2, 3, 4))(_.toString) shouldBe LazyList("1", "2", "3", "4")

    takeViaUnfold(LazyList(1, 2, 3, 4), 2) shouldBe LazyList(1, 2)
    takeViaUnfold(LazyList(1, 2, 3, 4), 0) shouldBe LazyList.empty[Int]
    takeViaUnfold(LazyList(1, 2, 3, 4), 8) shouldBe LazyList(1, 2, 3, 4)
    takeViaUnfold(LazyList.empty[Int], 8) shouldBe LazyList.empty[Int]

    takeWhileViaUnfold(LazyList(1, 2, 3, 4))(_ < 8) shouldBe LazyList(1, 2, 3, 4)
    takeWhileViaUnfold(LazyList(1, 2, 3, 4))(_ < 3) shouldBe LazyList(1, 2)

    zipWith(LazyList(2, 2, 2), LazyList(1, 2, 3))(_ + _) shouldBe LazyList(3, 4, 5)

    zipAll(LazyList(1, 2, 3), LazyList("a", "b", "c")) shouldBe LazyList((Some(1), Some("a")), (Some(2), Some("b")), (Some(3), Some("c")))
    zipAll(LazyList(1, 2), LazyList("a", "b", "c")) shouldBe LazyList((Some(1), Some("a")), (Some(2), Some("b")), (None, Some("c")))
    zipAll(LazyList(1, 2, 3), LazyList("a", "b")) shouldBe LazyList((Some(1), Some("a")), (Some(2), Some("b")), (Some(3), None))
  }

  "5.14" should "implement startsWith" in {
    startsWith(LazyList(1,2,3), LazyList(1,2)) shouldBe true
    startsWith(LazyList(1,2), LazyList(1,2,3)) shouldBe false
  }

}
