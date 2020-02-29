import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class FunctionDataStructureSpec extends AnyFlatSpec with Matchers {
  "2.6" should "match result" in {
    val x = List(1, 2, 3, 4, 5) match {
      case x :: 2 :: 4 :: _ => x
      case Nil => 42
      case x :: y :: 3 :: 4 :: _ => x + y
      case h :: t => h + 100
      case _ => 101
    }

    x shouldBe 3
  }

  "3.1" should "exam match result" in {
    val x = List(1, 2, 3, 4, 5) match {
      case x :: 2 :: 4 :: _ => x
      case Nil => 42
      case x :: y :: 3 :: 4 :: _ => x + y
      case h :: t => h + t.sum
      case _ => 101
    }

    x shouldBe 3
  }

  "3.2" should "delete head of list" in {
    def tail[A](input: List[A]): List[A] = input match {
      case Nil => sys.error("tail of empty list")
      case _ :: t => t
    }

    tail(List(1, 2, 3)) shouldBe List(2, 3)
    tail(List(1)) shouldBe Nil
  }

  "3.3" should "set head of list" in {
    def setHead[A](head: A, input: List[A]): List[A] = input match {
      case Nil => sys.error("set head of empty list")
      case _ :: t => head :: t
    }

    setHead(3, List(1, 2, 3)) shouldBe List(3, 2, 3)
    setHead("c", List("a", "b", "c")) shouldBe List("c", "b", "c")
  }

  "3.4" should "drop n from list" in {
    def drop[A](n: Int, input: List[A]): List[A] =
      if (n <= 0) input
      else
        input match {
          case Nil => Nil
          case _ :: t => drop(n - 1, t)
        }

    drop(1, List(1, 2, 3)) shouldBe List(2, 3)
    drop(0, List(1, 2, 3)) shouldBe List(1, 2, 3)
    drop(2, List("a", "b")) shouldBe Nil
    drop(3, List(1, 2)) shouldBe Nil
    drop(1, Nil) shouldBe Nil
  }

  "3.5" should "drop while from list" in {
    def dropWhile[A](input: List[A], f: A => Boolean): List[A] = input match {
      case Nil => Nil
      case h :: t => if (f(h)) dropWhile(t, f) else h :: dropWhile(t, f)
    }

    dropWhile(List(1, 2, 3, 4, 5), (a: Int) => a < 3) shouldBe List(3, 4, 5)
    dropWhile(List(1, 2, 3, 1, 5), (a: Int) => a < 3) shouldBe List(3, 5)
    dropWhile(List(1, 2, 3, 4, 5), (a: Int) => a < 0) shouldBe List(1, 2, 3, 4, 5)
    dropWhile(List(1, 2, 3, 4, 5), (a: Int) => a < 10) shouldBe Nil
    dropWhile(List(1, 2, 3, 4, 5), (a: Int) => a > 3) shouldBe List(1, 2, 3)
    dropWhile(List(1, 8, 3, 4, 5), (a: Int) => a > 3) shouldBe List(1, 3)
    dropWhile(Nil, (a: Int) => a > 3) shouldBe Nil
  }

  "3.6" should "init returns all elements except last for a list" in {
    def init[A](input: List[A]): List[A] = input match {
      case Nil => sys.error("init of empty list")
      case h :: Nil => Nil
      case h :: t => h :: init(t)
    }

    init(List(1, 2, 3)) shouldBe List(1, 2)
    init(List(1)) shouldBe Nil
  }
}
