import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.util.{Success, Try}


class HandlingErrorWithoutExceptionsSpec extends AnyFlatSpec with Matchers {

  sealed trait Option[+A] {
    def map[B](f: A => B): Option[B] = this match {
      case None => None
      case Some(a) => Some(f(a))
    }

    def getOrElse[B >: A](default: => B): B = this match {
      case None => default
      case Some(a) => a
    }

    def flatMap[B](f: A => Option[B]): Option[B] = map(f) getOrElse None

    def orElse[B >: A](ob: => Option[B]): Option[B] = this map (Some(_)) getOrElse ob

    def filter(f: A => Boolean): Option[A] = this match {
      case Some(a) if f(a) => this
      case _ => None
    }

  }

  case class Some[+A](get: A) extends Option[A]

  case object None extends Option[Nothing]

  "4.1" should "map works" in {
    case class Employee(name: String, department: String, manager: Option[String])

    def lookupByName(name: String): Option[Employee] = name match {
      case "Joe" => Some(Employee("Joe", "Finances", Some("Julie")))
      case "Mary" => Some(Employee("Mary", "IT", None))
      case "Izumi" => Some(Employee("Izumi", "IT", Some("Mary")))
      case _ => None
    }

    def getDepartment: (Option[Employee]) => Option[String] = _.map(_.department)

    getDepartment(lookupByName("Joe")) shouldBe Some("Finances")
    getDepartment(lookupByName("Mary")) shouldBe Some("IT")
    getDepartment(lookupByName("Foo")) shouldBe None

    def getManager: (Option[Employee]) => Option[String] = _.flatMap(_.manager)

    getManager(lookupByName("Joe")) shouldBe Some("Julie")
    getManager(lookupByName("Mary")) shouldBe None
    getManager(lookupByName("Foo")) shouldBe None

    lookupByName("Joe").filter(_.department != "IT") shouldBe Some(Employee("Joe", "Finances", Some("Julie")))
    lookupByName("Mary").filter(_.department != "IT") shouldBe None
    lookupByName("Foo").filter(_.department != "IT") shouldBe None
  }

  "4.2" should "variance" in {
    def mean(xs: Seq[Double]): Option[Double] =
      if (xs.isEmpty) None
      else Some(xs.sum / xs.length)

    def variance(xs: Seq[Double]): Option[Double] =
      mean(xs) flatMap (m => mean(xs.map(x => math.pow(x - m, 2))))
  }

  "4.3" should "implement map2" in {
    def map2[A, B, C](a: Option[A], b: Option[B])(f: (A, B) => C): Option[C] =
      a flatMap (aa => b map (bb => f(aa, bb)))

  }

  "4.4" should "implement sequence" in {
    def sequence[A](a: List[Option[A]]): Option[List[A]] = a match {
      case Nil => Some(Nil)
      case h :: t => h flatMap (hh => sequence(t) map (hh :: _))
    }

    sequence(List(Some(1), Some(2), Some(3))) shouldBe Some(List(1, 2, 3))
    sequence(List(Some(1), Some(2), None)) shouldBe None
  }

  "4.5" should "traverse implementations" in {
    def map2[A, B, C](a: Option[A], b: Option[B])(f: (A, B) => C): Option[C] =
      a flatMap (aa => b map (bb => f(aa, bb)))

    //with match
    def traverse[A, B](a: List[A])(f: A => Option[B]): Option[List[B]] = a match {
      case Nil => Some(Nil)
      case h :: t => map2(f(h), traverse(t)(f))(_ :: _)
    }

    //with flodRight
    def traverse_1[A, B](a: List[A])(f: A => Option[B]): Option[List[B]] =
      a.foldRight[Option[List[B]]](Some(Nil))((h, t) => map2(f(h), t)(_ :: _))

    def sequenceViaTraverse[A](a: List[Option[A]]): Option[List[A]] = traverse(a)(x => x)

    val list1 = List("1", "2", "3")
    val list2 = List("I", "II", "III", "IV")

    def parseInt(a: String): Option[Int] = Try(a.toInt) match {
      case Success(r) => Some(r)
      case _ => None
    }

    traverse(list1)(i => parseInt(i)) shouldBe Some(List(1, 2, 3))
    traverse(list2)(i => parseInt(i)) shouldBe None
  }

  "4.6" should "Either map, flatMap, orElse" in {
    case class Employee(name: String, department: String, manager: Option[String])
    def lookupByNameViaEither(name: String): Either[String, Employee] = name match {
      case "Joe" => Right(Employee("Joe", "Finances", Some("Julie")))
      case "Mary" => Right(Employee("Mary", "IT", None))
      case "Izumi" => Right(Employee("Izumi", "IT", Some("Mary")))
      case _ => Left("Employee not found")
    }

    def getDepartment: (Either[String, Employee]) => Either[String, String] =
      _.map(_.department)

    getDepartment(lookupByNameViaEither("Joe")) shouldBe Right("Finances")
    getDepartment(lookupByNameViaEither("Mary")) shouldBe Right("IT")
    getDepartment(lookupByNameViaEither("Foo")) shouldBe Left("Employee not found")

    def getManager(employee: Either[String, Employee]): Either[String, String] =
      employee.flatMap(e =>
        e.manager match {
          case Some(e) => Right(e)
          case _ => Left("Manager not found")
        })

    getManager(lookupByNameViaEither("Joe")) shouldBe Right("Julie")
    getManager(lookupByNameViaEither("Mary")) shouldBe Left("Manager not found")
    getManager(lookupByNameViaEither("Foo")) shouldBe Left("Employee not found")

    getManager(lookupByNameViaEither("Joe")).orElse(Right("Mr. CEO")) shouldBe Right("Julie")
    getManager(lookupByNameViaEither("Mary")).orElse(Right("Mr. CEO")) shouldBe Right("Mr. CEO")
    getManager(lookupByNameViaEither("Foo")).orElse(Right("Mr. CEO")) shouldBe Right("Mr. CEO")

  }
}
