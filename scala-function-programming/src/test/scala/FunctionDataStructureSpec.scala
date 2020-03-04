import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.annotation.tailrec

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

  "3.8" should "simulat foldRight" in {
    def foldRight[A, B](as: List[A], z: B)(f: (A, B) => B): B =
      as match {
        case h :: t => f(h, foldRight(t, z)(f))
        case Nil => z
      }

    foldRight(List(1, 2, 3, 4), Nil: List[Int])((a: Int, b: List[Int]) => a :: b) shouldBe List(1, 2, 3, 4)
    foldRight(List(1, 2, 3, 4), Nil: List[Int])(_ :: _) shouldBe List(1, 2, 3, 4)
  }

  "3.9" should "get length using foldRight" in {
    def length[A](as: List[A]): Int = as.foldRight(0)((_, b) => b + 1)

    length(List(1, 2, 3)) shouldBe 3
    length(List("a", "b", "c", "d")) shouldBe 4
    length(Nil) shouldBe 0
    length(List(1)) shouldBe 1
  }

  "3.10" should "foldLeft implementation" in {
    def foldLeft[A, B](as: List[A], z: B)(f: (B, A) => B): B =
      as match {
        case Nil => z
        case h :: t => foldLeft(t, f(z, h))(f)
      }

    foldLeft(List(1, 2, 3, 4), Nil: List[Int])((b: List[Int], a: Int) => a :: b) shouldBe List(4, 3, 2, 1)
  }

  "3.11" should "sum, product, length using foldLeft" in {
    def sum(input: List[Int]): Int =
      input.foldLeft(0)((acc, i) => acc + i)

    sum(List(1, 2, 3, 4)) shouldBe 10

    def product(input: List[Double]): Double =
      input.foldLeft(1.0)((acc, i) => acc * i)

    product(List(1.0, 2.0, 3.0)) shouldBe 6.0

    def length[A](input: List[A]): Int =
      input.foldLeft(0)((acc, _) => acc + 1)

    length(List(1, 2, 3, 4)) shouldBe 4
    length(List("a", "b", "c")) shouldBe 3
  }

  "3.12" should "reverse list using foldLeft" in {
    def reverse[A](input: List[A]): List[A] =
      input.foldLeft(Nil: List[A])((acc, i) => i :: acc)

    reverse(List(1, 2, 3, 4)) shouldBe List(4, 3, 2, 1)
    reverse(List("a", "b", "c")) shouldBe List("c", "b", "a")
  }

  "3.15" should "append using foldRight" in {
    def append[A](input: List[A], e: List[A]): List[A] =
      input.foldRight(e)((i, acc) => i :: acc)

    append(List(1, 2, 3), List(4, 5)) shouldBe List(1, 2, 3, 4, 5)
    append(Nil, List(4, 5)) shouldBe List(4, 5)
    append(List(4, 5), Nil) shouldBe List(4, 5)
    append(Nil, Nil) shouldBe Nil
  }

  "3.16" should "add 1" in {
    def increase(input: List[Int]): List[Int] =
      input.foldRight(Nil: List[Int])((i, acc) => i + 1 :: acc)

    increase(List(1, 2, 3, 4)) shouldBe List(2, 3, 4, 5)
  }

  "3.17" should "to string" in {
    def mapToString(input: List[Double]): List[String] =
      input.foldRight(Nil: List[String])((d, acc) => d.toString :: acc)

    mapToString(List(1.0, 2.0, 3.0)) shouldBe List("1.0", "2.0", "3.0")
  }

  "3.18" should "implement map" in {
    def map[A, B](input: List[A])(f: A => B): List[B] =
      input.foldRight(Nil: List[B])((e, acc) => f(e) :: acc)

    map(List(1, 2, 3))(_.toString) shouldBe List("1", "2", "3")
    map(List(1, 2, 3))(_ * 2.0) shouldBe List(2.0, 4.0, 6.0)
    map(List("How", "is", "GOING"))(_.length) shouldBe List(3, 2, 5)
  }

  "3.19" should "implement filter" in {
    def filter[A](input: List[A])(f: A => Boolean) =
      input.foldRight(Nil: List[A])((e, acc) => if (f(e)) acc else e :: acc)

    filter(List(1, 2, 3, 4, 5, 6))(_ % 2 == 1) shouldBe List(2, 4, 6)
  }

  "3.20" should "implement flatMap" in {
    def flatMap[A, B](as: List[A])(f: A => List[B]): List[B] =
      (as.foldRight(Nil: List[B])((e, acc) => f(e) ++ acc))

    flatMap(List(1, 2, 3))(i => List(i, i)) shouldBe List(1, 1, 2, 2, 3, 3)
  }

  "3.22" should "implement list add" in {
    def add(a: List[Int], b: List[Int]): List[Int] = (a, b) match {
      case (Nil, _) => Nil
      case (_, Nil) => Nil
      case (h1 :: t1, h2 :: t2) => (h1 + h2) :: add(t1, t2)
    }

    add(List(1, 2, 3), List(4, 5, 6)) shouldBe List(5, 7, 9)
    add(List(1, 2, 3), List(4, 5, 6, 9)) shouldBe List(5, 7, 9)
  }

  "3.23" should "implement zipWith" in {
    def zipWith[A, B, C](a: List[A], b: List[B])(f: (A, B) => C): List[C] = (a, b) match {
      case (Nil, _) => Nil
      case (_, Nil) => Nil
      case (h1 :: t1, h2 :: t2) => f(h1, h2) :: zipWith(t1, t2)(f)
    }

    zipWith(List(1, 2, 3), List("a", "b", "c"))((i, c) => s"$i: $c") shouldBe List("1: a", "2: b", "3: c")
  }

  "3.24" should "implement hasSubsequence" in {
    @tailrec
    def startsWith[A](l: List[A], prefix: List[A]): Boolean = (l, prefix) match {
      case (_, Nil) => true
      case (h1 :: t1, h2 :: t2) if (h1 == h2) => startsWith(t1, t2)
      case _ => false
    }

    @tailrec
    def hasSubsequence[A](sup: List[A], sub: List[A]): Boolean = sup match {
      case Nil => sub == Nil
      case _ if startsWith(sup, sub) => true //the if guard makes the difference
      case h :: t => hasSubsequence(t, sub)
    }

    def l = List(1, 2, 3, 4, 5)

    hasSubsequence(l, List(2, 3)) shouldBe true
    hasSubsequence(l, List(0, 1)) shouldBe false
    hasSubsequence(l, Nil) shouldBe true
  }

  "3.25" should "implement Tree size" in {
    def treeSize[A](t: Tree[A]): Int = t match {
      case Leaf(_) => 1
      case Branch(left, right) => 1 + treeSize(left) + treeSize(right)
    }

    treeSize(Leaf(1)) shouldBe 1
    treeSize(Branch(Leaf(1), Leaf(2))) shouldBe 3
    treeSize(Branch(Branch(Leaf(1), Leaf(1)), Leaf(2))) shouldBe 5
  }

  "3.26" should "implement Tree max" in {
    def treeMax(t: Tree[Int]): Int = t match {
      case Leaf(v) => v
      case Branch(left, right) => treeMax(left) max treeMax(right)
    }

    treeMax(Leaf(1)) shouldBe 1
    treeMax(Branch(Leaf(1), Leaf(2))) shouldBe 2
    treeMax(Branch(Branch(Leaf(1), Leaf(10)), Leaf(2))) shouldBe 10
  }

  "3.27" should "implement Tree Depth" in {
    def treeDepth[A](t: Tree[A]): Int = t match {
      case Leaf(_) => 0
      case Branch(left, right) => 1 + (treeDepth(left) max treeDepth(right))
    }

    treeDepth(Leaf(1)) shouldBe 0
    treeDepth(Branch(Leaf(1), Leaf(2))) shouldBe 1
    treeDepth(Branch(Branch(Leaf(1), Leaf(2)), Leaf(3))) shouldBe 2
  }

  "3.28" should "implement Tree Map" in {
    def treeMap[A, B](t: Tree[A])(f: A => B): Tree[B] = t match {
      case Leaf(a) => Leaf(f(a))
      case Branch(left, right) => Branch(treeMap(left)(f), treeMap(right)(f))
    }

    treeMap(Branch(Branch(Leaf(1), Leaf(2)), Leaf(3)))(_ * 2) shouldBe Branch(Branch(Leaf(2), Leaf(4)), Leaf(6))
  }

  "3.29" should "implement Tree Fold" in {
    def treeFold[A, B](t: Tree[A])(f: A => B)(g: (B, B) => B): B = t match {
      case Leaf(a) => f(a)
      case Branch(left, right) => g(treeFold(left)(f)(g), treeFold(right)(f)(g))
    }

    def sizeViaFold[A](t: Tree[A]): Int = treeFold(t)(a => 1)(1 + _ + _)

    def maxViaFold(t: Tree[Int]): Int = treeFold(t)(a => a)(_ max _)

    def depthViaFold[A](t: Tree[A]): Int = treeFold(t)(a => 0)((d1, d2) => 1 + (d1 max d2))

    def mapViaFold[A, B](t: Tree[A])(f: A => B): Tree[B] = treeFold(t)(a => Leaf(f(a)): Tree[B])(Branch(_, _))

    def t = Branch(Branch(Leaf(1), Leaf(2)), Leaf(3))

    sizeViaFold(t) shouldBe 5
    maxViaFold(t) shouldBe 3
    depthViaFold(t) shouldBe 2
    mapViaFold(t)(_ % 2 == 0) shouldBe Branch(Branch(Leaf(false), Leaf(true)), Leaf(false))
  }
}
