package example

import java.io.{FileInputStream, FileNotFoundException, FileOutputStream, IOException}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scala.util.control.Breaks

class Ch03Spec extends AnyFlatSpec with Matchers {

  it should "3.01" in {
    val a = Array("apple", "banana", "orange")
    for (e <- a) {
      println(e.toUpperCase)
    }

    val newArray = for (e <- a) yield e.toUpperCase
    newArray(0) shouldBe "APPLE"
    newArray(1) shouldBe "BANANA"
    newArray(2) shouldBe "ORANGE"

    for (i <- 0 until a.length) {
      a(i).toUpperCase shouldBe newArray(i)
    }

    for ((e, index) <- a.zipWithIndex) {
      println(s"$index is $e")
    }

    for (i <- 1 to 10 if i < 4) println(i)

    val names = Map("fname" -> "Robert", "lname" -> "Mike")
    for ((k, v) <- names) println(s"key: $k, value: $v")

    a.foreach(println)
  }

  it should "3.02" in {
    for (i <- 1 to 2; j <- 1 to 2) println(s"i=$i, j=$j")
    println("----------------------------------------")
    for {
      i <- 1 to 3
      j <- 1 to 2
      k <- 1 to 5
    } println(s"i=$i, j=$j, k=$k")
  }

  it should "3.03" in {
    for (i <- 1 to 10 if i % 2 == 0) println(i)
    for {
      i <- 1 to 10
      if i > 3
      if i < 6
      if i % 2 == 0
    } println(i)
  }

  it should "3.04" in {
    val names = Array("chris", "jenac", "tony")
    val capNames = for (e <- names) yield e.capitalize
    capNames.contains("Chris") shouldBe true
    capNames.contains("Jenac") shouldBe true
    capNames.contains("Tony") shouldBe true

    val lengths = for (e <- names) yield e.length
    lengths.foreach(println)
  }

  it should "3.05" in {
    import util.control.Breaks._
    var count = 0
    breakable {
      for (i <- 1 to 10) {
        count += 1
        if (i > 4) break
      }
    }
    count shouldBe 5

    val searchMe = "peter piper picked a peck of pickled peppers"
    var numPs = 0
    for (i <- 0 until searchMe.length) {
      breakable {
        if (searchMe.charAt(i) != 'p') {
          break //only breaks out breakable, still in loop, it simulates continue
        } else {
          numPs += 1
        }
      }
    }
    numPs shouldBe 9
    searchMe.count(_ == 'p') shouldBe 9

    println("----------nested break----------")
    val Inner = new Breaks
    val Outer = new Breaks
    Outer.breakable {
      for (i <- 1 to 5) {
        Inner.breakable {
          for (j <- 'a' to 'e') {
            if (i == 1 && j == 'c') Inner.break else println(s"i: $i, j: $j")
            if (i == 2 && j == 'b') Outer.break
          }
        }
      }
    }
  }

  it should "factorial" in {
    import scala.annotation.tailrec

    def factorial(n: Int): Int = {
      @tailrec def factorialAcc(acc: Int, n: Int): Int = {
        if (n <= 1) acc
        else factorialAcc(n * acc, n - 1)
      }

      factorialAcc(1, n)
    }

    factorial(3) shouldBe 6
    factorial(4) shouldBe 24
    factorial(5) shouldBe 120
  }

  it should "3.07 using match" in {
    def toMonth(i: Int) = i match {
      case 1 => "JAN"
      case 2 => "FEB"
      case 3 => "MAR"
      case 4 => "APR"
      case 5 => "MAY"
      case 6 => "JUN"
      case 7 => "JUL"
      case 8 => "AUG"
      case 9 => "SEP"
      case 10 => "OCT"
      case 11 => "NOV"
      case 12 => "DEC"
      case _ => "Invalid Month"
    }

    toMonth(1) shouldBe "JAN"
    toMonth(8) shouldBe "AUG"
    toMonth(16) shouldBe "Invalid Month"

    //map is better
    val monthNumberMap = Map(
      1 -> "JAN",
      2 -> "FEB",
      3 -> "MAR",
      4 -> "APR",
      5 -> "MAY",
      6 -> "JUN",
      7 -> "JUL",
      8 -> "AUG",
      9 -> "SEP",
      10 -> "OCT",
      11 -> "NOV",
      12 -> "DEC"
    )

    monthNumberMap(2) shouldBe "FEB"
    //    monthNumberMap(128) shouldBe "FEB" //throw exception
    monthNumberMap.get(128) shouldBe None
  }

  it should "3.08 multi case" in {
    def matchOddEven(i: Int) = i match {
      case 1 | 3 | 5 | 7 | 9 => "odd"
      case 2 | 4 | 6 | 8 | 10 => "even"
      case _ => "invalid"
    }

    matchOddEven(2) shouldBe "even"
  }

  it should "3.09 multi case return a value" in {
    def isTrue(a: Any) = a match {
      case 0 | "" => false
      case _ => true
    }

    isTrue(0) shouldBe false
    isTrue("xxx") shouldBe true
  }

  it should "3.10 default case can be _ or any variable name" in {
    def defaultCase(i: Int) = i match {
      case 1 => "One"
      case 2 => "Two"
      case anyValue => s"Unknown $anyValue"
    }

    defaultCase(1) shouldBe "One"
    defaultCase(128) shouldBe "Unknown 128"
  }

  it should "3.11 matching expression" in {
    case class Person(first: String, last: String)
    case class Dog(name: String)
    def echoWhatYouGaveMe(x: Any): String = x match {
      case 0 => "zero"
      case true => "true"
      case "hello" => "hello, world"
      case Nil => "empty List"
      case List(0, _, _) => "List with 0 as head, 3 elements"
      case List(0, _*) => "List with 0 as head, any number of elements"
      case Vector(1, _*) => "Vector with 0 as head, any number of elements"
      case (a, b) => s"Tuple ($a, $b)"
      case (a, b, c) => s"Tuple ($a, $b, $c)"
      case Person(first, "James") => s"$first, James"
      case Dog("Akka") => "dog named Akka"
      case s: String => s"got a String: $s"
      case i: Int => s"got a Int: $i"
      case f: Float => s"got a Float: $f"
      case a: Array[Int] => s"got a array of Int: ${a.mkString(",")}"
      case as: Array[String] => s"got a array of String: ${as.mkString(",")}"
      case d: Dog => s"got a dog: ${d.name}"
      case list: List[_] => s"got a List: $list"
      case m: Map[_, _] => m.toString
      case _ => "Unknown"
    }

    echoWhatYouGaveMe(List(Dog("A"), Dog("B"), Dog("C"))) shouldBe "got a List: List(Dog(A), Dog(B), Dog(C))"
    echoWhatYouGaveMe(Map("a" -> Dog("A"), "b" -> Dog("B"))) shouldBe "Map(a -> Dog(A), b -> Dog(B))"

    def patterWithVariable(x: Any): String = x match {
      case list@List(1, _*) => s"A: $list"
      case list: List[_] => s"B: $list"
      case _ => "unknown"
    }

    patterWithVariable(List(0, 1, 2)) shouldBe "B: List(0, 1, 2)"
    patterWithVariable(List(1, 2, 3)) shouldBe "A: List(1, 2, 3)"
  }

  it should "3.12 matching case class" in {
    trait Animal
    case class Dog(name: String) extends Animal
    case class Cat(name: String) extends Animal
    case object WoodPecker extends Animal

    def whatAnimal(x: Animal): String = x match {
      case Dog(dog) => s"Dog: $dog"
      case _: Cat => "Just a Cat"
      case WoodPecker => "A WoodPecker"
      case _ => "Other Animals"
    }

    whatAnimal(Dog("haha")) shouldBe "Dog: haha"
    whatAnimal(Cat("whatever")) shouldBe "Just a Cat"
    whatAnimal(WoodPecker) shouldBe "A WoodPecker"
  }

  it should "3.13 case if" in {
    case class Person(name: String)
    def speak(p: Person) = p match {
      case Person(name) if name == "Trump" => "MAGA"
      case Person(name) if name == "Obama" => "AAAA"
      case _ => "Nothing"
    }

    speak(Person("Trump")) shouldBe "MAGA"
    speak(Person("Anyone")) shouldBe "Nothing"
  }

  it should "3.14 match instance of" in {
    case class Member()
    def isMember(p: Any) = p match {
      case m: Member => true
      case _ => false
    }

    isMember(Member()) shouldBe true
    isMember("What") shouldBe false
  }

  it should "3.15 matching list" in {
    val x = List(1, 2, 3)
    val y = 1 :: 2 :: 3 :: Nil
    x shouldBe y

    def headAndRest(v: List[String]): String = v match {
      case h :: r => s"header: $h, rest: $r"
      case Nil => "nil"
    }

    headAndRest("Apple" :: "Banana" :: "Orange" :: Nil) shouldBe "header: Apple, rest: List(Banana, Orange)"
    headAndRest(Nil) shouldBe "nil"

    def sum(list: List[Int]): Int = list match {
      case Nil => 0
      case n :: rest => n + sum(rest)
    }

    sum(List(1, 2, 3)) shouldBe 6

    def multiply(list: List[Int]): Int = list match {
      case Nil => 1
      case n :: rest => n * multiply(rest)
    }

    multiply(List(1, 2, 3, 4, 5)) shouldBe 120
  }

  it should "3.16 matching in try/catch" in {
    //simulate exception throw
    def openFile(s: String) = s match {
      case "io" => throw new IOException("mocked")
      case "notFound" => throw new FileNotFoundException("mocked")
      case _ => throw new Exception("mocked")
    }

    def tryOpenFile(s: String) = try {
      openFile(s)
    } catch {
      case e: FileNotFoundException => "FileNotFound"
      case e: IOException => "IOException"
      case _: Throwable => "Exception"
    }

    tryOpenFile("io") shouldBe "IOException"
    tryOpenFile("notFound") shouldBe "FileNotFound"
    tryOpenFile("a") shouldBe "Exception"
  }

  it should "3.17 try/catch/finally" in {
    //code demo only
    //copy file
    var in = None: Option[FileInputStream]
    var out = None: Option[FileOutputStream]

    try {
      in = Some(new FileInputStream("/tmp/a.txt"))
      out = Some(new FileOutputStream("/tmp/b.txt"))

      var c = 0
      while ( {
        c = in.get.read; c != 1
      }) {
        out.get.write(c)
      }
    } catch {
      case e: IOException => e.printStackTrace()
    } finally {
      println("enter finally...")
      if (in.isDefined) in.get.close()
      if (out.isDefined) out.get.close()
    }

    //better way:
    //    try {
    //      in = Some(new FileInputStream("/tmp/a.txt"))
    //      out = Some(new FileOutputStream("/tmp/b.txt"))
    //      in.foreach( inputStream =>
    //        out.foreach {
    //          outStream =>
    //            var c = 0
    //            while ({c=inputStream.read; c!= 1}) {
    //              outStream.write(c)
    //            }
    //        })
    //    } catch ...
  }
}
