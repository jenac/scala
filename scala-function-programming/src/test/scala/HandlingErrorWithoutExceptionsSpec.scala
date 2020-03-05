import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers


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

//    def variance(xs: Seq[Double]): Option[Double] = mean(xs) flatMap(m=> )
  }
}
