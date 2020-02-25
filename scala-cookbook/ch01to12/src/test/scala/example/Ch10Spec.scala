package example

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scala.collection.mutable.ArrayBuffer

class Ch10Spec extends AnyFlatSpec with Matchers {
  "10.11" should "zipWithInex and zip" in {
    val days = Array("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    val zipped = days.zipWithIndex
    zipped shouldBe Array(("Sun", 0), ("Mon", 1), ("Tue", 2), ("Wed", 3), ("Thu", 4), ("Fri", 5), ("Sat", 6))

    val lower = Array("a", "b", "c")
    val upper = Array("A", "B", "C")
    val lu = lower.zip(upper)
    lu shouldBe Array(("a", "A"), ("b", "B"), ("c", "C"))
  }

  "10.15" should "flatten" in {
    val lol = List(List(1, 2), List(3, 4, 5))
    val fl = lol.flatten
    fl shouldBe List(1, 2, 3, 4, 5)

    val loa = List(Array(1, 2), Array(3, 4))
    val fla = loa.flatten
    fla shouldBe List(1, 2, 3, 4)
  }

  "10.16" should "flatmap" in {
    val bags = List("1", "2", "three", "4", "too much")

    def toInt(in: String): Option[Int] = {
      try {
        Some(Integer.parseInt((in.trim)))
      } catch {
        case e: Exception => None
      }
    }

    val sum = bags.flatMap(toInt).sum
    sum shouldBe 7

    //flatMap remove Nones
    val fm = bags.flatMap(toInt)
    fm shouldBe List(1, 2, 4)
  }

  "10.17" should "filter" in {
    val list = "apple" :: "banana" :: 1 :: 2 :: Nil
    val strings = list.filter {
      case s: String => true
      case _ => false
    }
    strings shouldBe List("apple", "banana")
  }

  "10.18" should "pick elements methods" in {
    val x = (1 to 10).toArray

    val y = x.drop(3)
    y shouldBe Array(4, 5, 6, 7, 8, 9, 10)

    val z = x.dropWhile(_ < 6)
    z shouldBe Array(6, 7, 8, 9, 10)

    val a = x.dropRight(4)
    a shouldBe Array(1, 2, 3, 4, 5, 6)

    val b = x.take(3)
    b shouldBe Array(1, 2, 3)

    val c = x.takeWhile(_ < 5)
    c shouldBe Array(1, 2, 3, 4)

    val d = x.takeRight(3)
    d shouldBe Array(8, 9, 10)

    //slice(from, until) until not included
    val peeps = List("John", "Mary", "Jane", "Fred")
    peeps.slice(1, 3) shouldBe List("Mary", "Jane")

    val nums = Array(1, 2, 3, 4, 5)
    nums.head shouldBe 1
    nums.headOption shouldBe Some(1)
    nums.init shouldBe Array(1, 2, 3, 4)
    nums.last shouldBe 5
    nums.lastOption shouldBe Some(5)
    nums.tail shouldBe Array(2, 3, 4, 5)
  }


  "10.19" should "groupby, partition" in {
    val x = List(15, 10, 5, 8, 20, 12)

    val g = x.groupBy(_ > 10)
    g shouldBe Map(false -> List(10, 5, 8), true -> List(15, 20, 12))
    g(true) shouldBe List(15, 20, 12)

    val p = x.partition(_ > 10)
    p shouldBe(List(15, 20, 12), List(10, 5, 8))

    val s = x.span(_ < 20) //span: for collection t and function f, return ( t takeWhile f, t dropWhile f)
    s shouldBe(List(15, 10, 5, 8), List(20, 12))

    val sp = x.splitAt(2)
    sp shouldBe(List(15, 10), List(5, 8, 20, 12))

    val nums = (1 to 5).toList
    nums shouldBe List(1, 2, 3, 4, 5)

    val s1 = nums.sliding(2).toList
    s1 shouldBe List(List(1, 2), List(2, 3), List(3, 4), List(4, 5))

    val s2 = nums.sliding(3).toList
    s2 shouldBe List(List(1, 2, 3), List(2, 3, 4), List(3, 4, 5))

    //step = 2
    val s3 = nums.sliding(2, 2).toList
    s3 shouldBe List(List(1, 2), List(3, 4), List(5))

    //step = 3
    val s4 = nums.sliding(2, 3).toList
    s4 shouldBe List(List(1, 2), List(4, 5))
  }

  "10.19" should "mutable/immutable collections" in {
    List(1, 2) shouldBe List(1, 2)
    List(List(1, 2)) shouldBe List(List(1, 2))
    Array(1, 2) shouldBe Array(1, 2)
    //since array is mutable, the following will be List(somePointer) vs List(otherPointer)
    List(Array(1, 2)) == List(Array(1, 2)) shouldBe false
  }

  "10.19" should "zip/unzip" in {
    val num = List(1, 2)
    val eng = List("One", "Two")

    val z = num zip eng
    z shouldBe List((1, "One"), (2, "Two"))

    val uz = z.unzip
    uz shouldBe(List(1, 2), List("One", "Two"))
  }

  "10.20" should "go through collection and compare adjacent elements" in {
    val nums = Array(1, 2, 3, 4, 5)
    val sum = nums.reduceLeft(_ + _)
    sum shouldBe 15

    val product = nums.reduceLeft(_ * _)
    product shouldBe 120

    val min = nums.reduceLeft(_ min _)
    min shouldBe 1

    val max = nums.reduceLeft(_ max _)
    max shouldBe 5

    val floats = Array(1.0, 2.0, 3.0)
    val left = floats.reduceLeft(_ / _)
    left shouldBe 0.16666666666666666 //(1.0/2.0)/3.0
    val right = floats.reduceRight(_ / _)
    right shouldBe 1.5 //(3.0/2.0)/1.0

    //seed = 1
    val sl = nums.scanLeft(1)(_ * _)
    sl shouldBe Array(1, 1, 2, 6, 24, 120)
    val sr = nums.scanRight(1)(_ * _)
    sr shouldBe Array(120, 120, 60, 20, 5, 1)
    //seed = 10
    val sl10 = nums.scanLeft(10)(_ * _)
    sl10 shouldBe Array(10, 10, 20, 60, 240, 1200)
    val sr10 = nums.scanRight(10)(_ * _)
    sr10 shouldBe Array(1200, 1200, 600, 200, 50, 10)
  }

  "10.22" should "merge 2 collections" in {
    val a = ArrayBuffer(1, 2, 3) //mutable
    a ++= Seq(4, 5)
    a shouldBe ArrayBuffer(1, 2, 3, 4, 5) //immutable

    val b = Array(1, 2, 3) //immutable
    val c = Array(4, 5, 6) //immutable
    val d = b ++ c
    d shouldBe Array(1, 2, 3, 4, 5, 6)

    val a1 = Array(1, 2, 3, 4, 5)
    val b1 = Array(4, 5, 6, 7, 8)
    val c1 = a1.intersect(b1)
    c1 shouldBe Array(4, 5)
    val d1 = a1.union(b1)
    d1 shouldBe Array(1, 2, 3, 4, 5, 4, 5, 6, 7, 8)
    val d2 = a1.union(b1).distinct
    d2 shouldBe Array(1, 2, 3, 4, 5, 6, 7, 8)
    val e1 = a1.diff(b1)
    e1 shouldBe Array(1, 2, 3)
    val e2 = b1.diff(a1)
    e2 shouldBe Array(6, 7, 8)

    val f1 = Array.concat(a1, b1)
    f1 shouldBe Array(1, 2, 3, 4, 5, 4, 5, 6, 7, 8)

    val l1 = List(1, 2, 3)
    val l2 = List(4, 5, 6)
    val l3 = l1 ::: l2
    l3 shouldBe List(1, 2, 3, 4, 5, 6)
  }

  "10.23" should "zip igore longer one" in {
    val products = List("pizza", "soda", "fruits")
    val prices = List(5.99, 0.99, 1.99, 3.27) //3.27 will be ignored
    val productAndPrices = products.zip(prices)
    productAndPrices shouldBe List(("pizza", 5.99), ("soda", 0.99), ("fruits", 1.99))
  }

  "10.24" should "view usage" in {
    println("starting...")
    val instant = (1 to 1000).map { e =>
      Thread.sleep(10)
      e * 2
    }
    println("instant takes time")

    val view = (1 to 1000).view.map { e =>
      Thread.sleep(10)
      e * 2
    }
    println(("view returns immediately"))
  }

  "10.25" should "use range" in {
    val a = Array.range(1, 5)
    a shouldBe Array(1, 2, 3, 4)

    val b = List.range(1, 10)
    b shouldBe List(1, 2, 3, 4, 5, 6, 7, 8, 9)

    val c = Vector.range(0, 10, 2)
    c shouldBe Vector(0, 2, 4, 6, 8)

    val d = (1 to 5).toList
    d shouldBe List(1, 2, 3, 4, 5)

    val e = (1 until 5).toList
    e shouldBe List(1, 2, 3, 4)

    val f = (1 to 10).by(2).toList
    f shouldBe List(1, 3, 5, 7, 9)

    val g = ('a' to 'f').toList
    g shouldBe List('a', 'b', 'c', 'd', 'e', 'f')
  }

  "10.28" should "using sort" in {
    val rawNums = List(3, 6, 2, 9, 1)
    val a = rawNums.sorted
    a shouldBe List(1, 2, 3, 6, 9)

    val b = rawNums.sortWith(_ > _)
    b shouldBe List(9, 6, 3, 2, 1)

    val raw = List("banana", "pear", "apple", "orange")
    val c = raw.sorted
    c shouldBe List("apple", "banana", "orange", "pear")

    val d = raw.sortWith(_ > _)
    d shouldBe List("pear", "orange", "banana", "apple")

    val e = raw.sortWith(_.length < _.length)
    e shouldBe List("pear", "apple", "banana", "orange")

    class Person(var name: String) extends Ordered[Person] {
      override def compare(that: Person): Int = {
        if (this.name == that.name)
          0
        else if (this.name > that.name)
          1
        else
          -1
      }
    }

    val p1 = new Person("Tyler")
    val p2 = new Person("James")
    val p3 = new Person("Tom")
    val p4 = new Person("Peter")
    val personList = List(p1, p2, p3, p4)
    val personSorted = personList.sorted
    personSorted shouldBe List(p2, p4, p3, p1)
  }

  "10.29" should "mkString" in {
    val a = Array("apple", 7897, "orange", 1234)
    val s = a.mkString
    s shouldBe "apple7897orange1234"
    val sp = a.mkString(",")
    sp shouldBe "apple,7897,orange,1234"
    val swithpresufix = a.mkString("{", ",", "}")
    swithpresufix shouldBe "{apple,7897,orange,1234}"
  }
}
