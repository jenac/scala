package example

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scala.collection.immutable.{ListMap, SortedSet}
import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, ListBuffer}

class Ch11Spec extends AnyFlatSpec with Matchers {
  "11.01" should "create List" in {
    val a = 1 :: 2 :: 3 :: Nil
    a shouldBe List(1, 2, 3)

    val b = List(1, 2, 3)
    b shouldBe List(1, 2, 3)

    val c = List(1.0, 2.0, 33D, 4000.0)
    c shouldBe List(1.0, 2.0, 33D, 4000.0)

    val x = List.range(1, 10)
    x shouldBe List(1, 2, 3, 4, 5, 6, 7, 8, 9)

    val d = List.fill(3)("how")
    d shouldBe List("how", "how", "how")

    val e = List.tabulate(5)(n => n * n)
    e shouldBe List(0, 1, 4, 9, 16)

    val f = collection.mutable.ListBuffer(1, 2, 3).toList
    f shouldBe List(1, 2, 3)

    val g = "too".toList
    g shouldBe List('t', 'o', 'o')
  }

  "11.02" should "create mutable List" in {
    val fruits = new ListBuffer[String]()
    fruits += "Apple"
    fruits += "Orange"
    fruits += "Banana"
    fruits += ("StrawBerry", "Kiwi", "Pineapple")

    fruits -= "Orange"
    fruits -= ("Apple", "Pineapple")

    val list = fruits.toList
    list shouldBe List("Banana", "StrawBerry", "Kiwi")
  }

  "11.03" should "add element to list from head" in {
    val x = List(2)
    val y = 1 :: x
    y shouldBe List(1, 2)

    //will the following slow?
    val z = 0 +: y
    z shouldBe List(0, 1, 2)

    val u = x :+ 2
    u shouldBe List(2, 2)
  }

  "11.04" should "delete from List/ListBuffer" in {
    val org = List(5, 1, 4, 3, 2)
    val a = org.filter(_ > 2)
    a shouldBe List(5, 4, 3)

    //if need frequent change use ListBuffer
    val x = ListBuffer(1, 2, 3, 4, 5, 6, 7, 8, 9)
    x -= 5
    x -= (2, 3)
    x shouldBe List(1, 4, 6, 7, 8, 9)

    val e = x.remove(0)
    e shouldBe 1
    x shouldBe List(4, 6, 7, 8, 9)

    x.remove(1, 3)
    x shouldBe ListBuffer(4, 9)

    val xx = ListBuffer(1, 2, 3, 4, 5, 6)
    xx --= Seq(2, 3, 4)
    xx.toList shouldBe List(1, 5, 6)
  }

  "11.05" should "merge list" in {
    val a = List(1, 2, 3)
    val b = List(4, 5, 6)

    val c = a ::: b
    c shouldBe List(1, 2, 3, 4, 5, 6)

    val d = a ++ b
    d shouldBe List(1, 2, 3, 4, 5, 6)

    val e = List.concat(a, b)
    e shouldBe List(1, 2, 3, 4, 5, 6)
  }

  "11.06" should "List lazy evaluated version, Stream" in {
    //before 2.13, use stream
    // val stream = 1 #:: 2 #:: 3 #:: Stream.empty

    val list = 1 #:: 2 #:: 3 #:: LazyList.empty
    lazy val naturals: LazyList[Int] = 1 #:: naturals.map(_ + 1)
    naturals.take(10).mkString(",") shouldBe "1,2,3,4,5,6,7,8,9,10"
    //naturals.filter(_<200) //this is still a stream.

    lazy val fibs: LazyList[BigInt] = BigInt(0) #:: BigInt(1) #:: fibs.zip(fibs.tail).map { n => n._1 + n._2 }
    fibs.take(19).mkString(",") shouldBe "0,1,1,2,3,5,8,13,21,34,55,89,144,233,377,610,987,1597,2584"
  }

  "11.07" should "create Array" in {
    val a = Array(1, 2, 3)
    val b = Array("A", "B", "CD")

    val c = Array(1, 2.0, 33D, 400L) //Array[Double]

    val d = new Array[String](3) //initialize with size, then fill it
    d(0) = "AA"
    d(1) = "AB"
    d(2) = "AC"
    d shouldBe Array("AA", "AB", "AC")

    val e = Array.range(1, 5)
    val f = Array.range(1, 5, 2) //with step

    val g = Array.fill(3)("OK")
    g shouldBe Array("OK", "OK", "OK")

    val h = Array.tabulate(5)(n => n * n)
    h shouldBe Array(0, 1, 4, 9, 16)
  }

  "11.08" should "ArrayBuffer can change size" in {
    var characters = ArrayBuffer[String]()
    characters += "Ben"
    characters += "Jerry"
    characters += ("A", "B")
    characters ++= Seq("C", "F", "E")
    characters.append("F")
    characters.appendAll(List("G", "H", "I"))
    characters.mkString(",") shouldBe "Ben,Jerry,A,B,C,F,E,F,G,H,I"
  }

  "11.09" should "remove from ArrayBuffer and Array" in {
    val a = ArrayBuffer("a", "b", "c", "d", "e")
    a -= "a"
    a -= ("b", "d")
    a shouldBe ArrayBuffer("c", "e")

    val b = ArrayBuffer("a", "b", "c", "d", "e")
    b --= Seq("a", "c", "d")
    b shouldBe ArrayBuffer("b", "e")

    val c = ArrayBuffer("a", "b", "c", "d", "e")
    c --= Array("a", "c")
    c shouldBe ArrayBuffer("b", "d", "e")

    val d = ArrayBuffer("a", "b", "c", "d", "e")
    d --= Set("a", "c", "d")
    d shouldBe ArrayBuffer("b", "e")
  }

  "11.11" should "multi-dimension array" in {
    val rows = 2
    val cols = 3
    val a = Array.ofDim[String](rows, cols)
    a(0)(0) = "AA"
    a(0)(1) = "AB"
    a(0)(2) = "AC"
    a(1)(0) = "AD"
    a(1)(1) = "AE"
    a(1)(2) = "AF"

    for {
      i <- 0 until rows
      j <- 0 until cols
    } println(s"($i, $j) is ${a(i)(j)}")
  }

  "11.12" should "create map" in {
    val s1 = Map("AL" -> "Alabama", "AK" -> "Alaska")
    val s2 = Map(("AL", "Alabama"), ("AK", "Alaska"))
    (s1 == s2) shouldBe true
  }

  "11.14" should "add/remove from mutable map" in {
    var s1 = mutable.Map[String, String]()
    s1("AK") = "Alasaka"
    s1 += ("MN" -> "Minnesota")
    s1 += ("AK" -> "Alaska")
    s1 shouldBe mutable.HashMap("MN" -> "Minnesota", "AK" -> "Alaska")

    s1 += ("A" -> "a", "B" -> "b")
    s1 ++= List("C" -> "c", "D" -> "d")
    s1 shouldBe mutable.HashMap("A" -> "a", "MN" -> "Minnesota", "B" -> "b", "C" -> "c", "D" -> "d", "AK" -> "Alaska")

    s1 -= "A"
    s1 --= List("MN", "AK")
    s1 shouldBe mutable.HashMap("B" -> "b", "C" -> "c", "D" -> "d")

    s1.put("NY", "New York")
    s1 shouldBe mutable.HashMap("B" -> "b", "C" -> "c", "D" -> "d", "NY" -> "New York")

    s1.clear()
    s1 shouldBe Map()
  }

  "11.15" should "add/remove from immutable map" in {
    val a = Map("A" -> "a")
    val b = a + ("B" -> "b")
    b shouldBe Map("A" -> "a", "B" -> "b")
    val c = a + ("C" -> "c", "D" -> "d")
    c shouldBe Map("A" -> "a", "C" -> "c", "D" -> "d")
    val d = c + ("A" -> "changed")
    d shouldBe Map("A" -> "changed", "C" -> "c", "D" -> "d")
    val e = b - "A"
    e shouldBe Map("B" -> "b")
    val f = c - "A" - "D"
    f shouldBe Map("C" -> "c")
    val g = f - "A"
    g shouldBe Map("C" -> "c")
  }

  "11.16" should "map get value by key" in {
    val s = Map("A" -> "a", "B" -> "b", "C" -> "c")
    val a = s("A")
    a shouldBe "a"
    //    val d = s("D") //throw exception
    val d1 = s.get("D")
    d1 shouldBe None
    val a1 = s.get("A")
    a1 shouldBe Some("a")
  }

  "11.17" should "iterate map" in {
    val ratings = Map("A" -> 3.0, "B" -> 2.5, "C" -> 3.8)
    for ((k, v) <- ratings) println(s"$k = $v")
    println("----------")
    ratings.foreach {
      case (name, score) => println(s"key: $name, value: $score")
    }
    println("----------")
    ratings.foreach(x => println(s"1-${x._1}, 2-${x._2}"))

    ratings.keys.mkString(",") shouldBe "A,B,C"
    ratings.values.mkString("|") shouldBe "3.0|2.5|3.8"

    val increased = ratings.view.mapValues(n => n + 10).toMap
    increased shouldBe Map("A" -> 13.0, "B" -> 12.5, "C" -> 13.8)
  }

  "11.19" should "reverse key/value" in {
    val source = Map("A" -> 1, "B" -> 2, "C" -> 1)
    val reverse = for ((k, v) <- source) yield (v, k)
    reverse shouldBe Map(1 -> "C", 2 -> "B")
  }

  "11.20" should "check key/value exists" in {
    val source = Map("A" -> 1, "B" -> 2, "C" -> 3)
    val a = source.keys.exists(_ == "A")
    a shouldBe true
    val b = source.keys.exists(_ == "F")
    b shouldBe false
    val c = source.values.exists(_ == 2)
    c shouldBe true
    val d = source.values.exists(_ == 12)
    d shouldBe false
  }

  "11.21" should "filter map" in {
    //mutable
    val m = mutable.Map(1 -> "A", 2 -> "B", 3 -> "C")
    m.filterInPlace((k, v) => k > 1)
    m shouldBe mutable.HashMap(2 -> "B", 3 -> "C")
    m.mapValuesInPlace((k, v) => (k + 10).toString)
    m shouldBe mutable.HashMap(2 -> "12", 3 -> "13")

    //immutable
    val im = Map(1 -> "A", 2 -> "B", 3 -> "C")
    val f = im.view.filterKeys(_ > 1).toMap
    f shouldBe Map(2 -> "B", 3 -> "C")
    val g = im.view.mapValues(v => v + "ooo").toMap
    g shouldBe Map(1 -> "Aooo", 2 -> "Booo", 3 -> "Cooo")
  }

  "11.22" should "sorting key or values" in {
    val grades = Map("K" -> 90,
      "A" -> 85,
      "M" -> 95,
      "E" -> 91,
      "H" -> 92
    )

    val sortedByKey = ListMap(grades.toSeq.sortBy(_._1): _*)
    sortedByKey shouldBe ListMap("A" -> 85, "E" -> 91, "H" -> 92, "K" -> 90, "M" -> 95)

    val sortedByKeyDesc = ListMap(grades.toSeq.sortWith(_._1 > _._1): _*)
    sortedByKeyDesc shouldBe ListMap("M" -> 95, "K" -> 90, "H" -> 92, "E" -> 91, "A" -> 85)

    val sortedByValue = ListMap(grades.toSeq.sortBy(_._2): _*)
    sortedByValue shouldBe ListMap("A" -> 85, "K" -> 90, "E" -> 91, "H" -> 92, "M" -> 95)

    val sortedByValueDesc = ListMap(grades.toSeq.sortWith(_._2 > _._2): _*)
    sortedByValueDesc shouldBe ListMap("M" -> 95, "H" -> 92, "E" -> 91, "K" -> 90, "A" -> 85)

    //about _*
    val fruits = List("apple", "banana", "cherry")

    def printAll(strings: String*): Unit = {
      strings.foreach(println)
    }
    //printAll(fruits) //compile error
    printAll(fruits: _*)
  }

  "11.23" should "find max key or value" in {
    val grades = Map("Kity" -> 90,
      "Amy" -> 85,
      "M" -> 95,
      "Emerson" -> 91,
      "House" -> 92
    )

    val a = grades.max
    a shouldBe("M", 95)
    val b = grades.keysIterator.max
    b shouldBe "M"
    val c = grades.keysIterator.reduceLeft((x, y) => if (x > y) x else y)
    c shouldBe "M"
    val d = grades.keysIterator.reduceLeft((x, y) => if (x.length > y.length) x else y)
    d shouldBe "Emerson"

    val e = grades.valuesIterator.max
    e shouldBe 95
    val f = grades.valuesIterator.reduceLeft(_ max _)
    f shouldBe 95
    val g = grades.valuesIterator.reduceLeft((x, y) => if (x > y) y else x)
    g shouldBe 85
  }

  "11.24" should "adding elements to Set" in {
    //mutable
    var a = mutable.Set[Int]()
    a += 1
    a shouldBe mutable.HashSet(1)
    a += (2, 3)
    a shouldBe mutable.HashSet(1, 2, 3)
    a += 2
    a shouldBe mutable.HashSet(1, 2, 3)
    a ++= Vector(4, 5)
    a shouldBe mutable.HashSet(1, 2, 3, 4, 5)
    val b = a.add(6)
    b shouldBe true
    a shouldBe mutable.HashSet(1, 2, 3, 4, 5, 6)
    val c = a.add(6)
    c shouldBe false
    a.contains(4) shouldBe true
    a.contains(12) shouldBe false

    //immutable
    val ia = Set(1, 2)
    val ib = ia + 3
    ib shouldBe Set(1, 2, 3)
    val ic = ib + (4, 5)
    ic shouldBe Set(1, 2, 3, 4, 5)
    val id = ic ++ List(6, 7)
    id shouldBe Set(1, 2, 3, 4, 5, 6, 7)
  }

  "11.25" should "remove elements from Set" in {
    //mutable
    var a = mutable.Set(1, 2, 3, 4, 5, 6)
    a -= 2
    a shouldBe mutable.HashSet(1, 3, 4, 5, 6)
    a -= (1, 4)
    a shouldBe mutable.HashSet(3, 5, 6)
    a --= List(3, 6)
    a shouldBe mutable.HashSet(5)

    var b = mutable.Set(1, 2, 3, 4, 5, 6)
    b.filterInPlace(_ > 3)
    b shouldBe mutable.HashSet(4, 5, 6)
    b.clear()
    b shouldBe mutable.HashSet()

    var c = mutable.Set(11, 12, 13, 14, 15, 16)
    val d = c.remove(12)
    d shouldBe true
    val e = c.remove(200)
    e shouldBe false
    c shouldBe mutable.HashSet(16, 11, 13, 14, 15)

    //immutable
    val ia = Set(1, 2, 3, 4, 5, 6)
    val ib = ia - 2
    ib shouldBe Set(5, 1, 6, 3, 4)
    val ic = ib -- Array(5, 3)
    ic shouldBe Set(1, 6, 4)
    val id = Set(1, 2, 3, 4, 5, 6)
    val ie = id.filter(_ < 5)
    ie shouldBe Set(1, 2, 3, 4)
    val ig = ie.take(2)
    ig shouldBe Set(1, 2)
  }

  "11.26" should "use sorted set" in {
    val a = SortedSet(2, 5, 6, 74, 13, 53)
    a shouldBe SortedSet(2, 5, 6, 13, 53, 74)

    val b = SortedSet("Tree", "USA", "Dyson11")
    b shouldBe SortedSet("Dyson11", "Tree", "USA")

    //class muster extends Ordered to by put into SortedSet
    class Person(val name: String) extends Ordered[Person] {
      override def compare(that: Person): Int = {
        if (this.name == that.name)
          0
        else if (this.name > that.name)
          1
        else
          -1
      }
    }
    val molly = new Person("Molly")
    val kelly = new Person("Kelly")
    val john = new Person("John")
    val jason = new Person("Jason")
    val c = SortedSet(molly, kelly, john, jason)
    c shouldBe SortedSet(jason, john, kelly, molly)
  }

  "11.27" should "use queue" in {
    val a = mutable.Queue(1, 2, 4)
    a += 5
    a.enqueue(7)
    a += (4, 9)
    a ++= List(10, 11, 24)
    a shouldBe mutable.Queue(1, 2, 4, 5, 7, 4, 9, 10, 11, 24)

    val b = a.dequeue()
    b shouldBe 1
    val c = a.dequeueFirst(_ > 8)
    c shouldBe Some(9)
    val d = a.dequeueFirst(_ > 100)
    d shouldBe None
    a shouldBe mutable.Queue(2, 4, 5, 7, 4, 10, 11, 24)

    a.dequeueAll(_ > 10)
    a shouldBe mutable.Queue(2, 4, 5, 7, 4, 10)
  }

  "11.28" should "use stack" in {
    var fruits = mutable.Stack[String]()
    fruits.push("apple")
    fruits.push("banana")
    fruits.push("cherry")
    val a = fruits.pop()
    a shouldBe "cherry"
    val b = fruits.top
    b shouldBe "banana"
    fruits shouldBe mutable.Stack("banana", "apple")
  }
}
