import org.mockito.scalatest.MockitoSugar
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.{ExecutionContext, Future}

class MiscSpec extends AnyFlatSpec with Matchers with MockitoSugar {
  it should "foldLeft vs foldRight" in {
    val values = List(1, 2, 3, 4, 5)
    val left = values.foldLeft(List.empty[Int])((acc, e) => e :: acc)
    val right = values.foldRight(List.empty[Int])((e, acc) => e :: acc)
    left shouldBe List(5, 4, 3, 2, 1)
    right shouldBe List(1, 2, 3, 4, 5)
  }

  it should "collect all left values" in {
    val values = List(Left("l-1"), Right("r-1"), Left("l-2"))
    val r1 = values.filter(_.isLeft).map(_.left.getOrElse(None))
    val r2 = values.map(_.left.getOrElse(None))
    val r3 = values.collect(_.left.getOrElse(None))
    r1 shouldBe List("l-1", "l-2")
    r2 shouldBe List("l-1", None, "l-2")
    r3 shouldBe List("l-1", None, "l-2")

    val lefts = values.foldRight(List.empty[String])((i, acc) => i match {
      case Left(value) => value :: acc
      case Right(_) => acc
    })
    lefts shouldBe List("l-1", "l-2")

    val lefts2 = values.collect { case Left(v) => v}
    lefts2 shouldBe List("l-1", "l-2")
  }

  it should "seq of future" in {
    case class Person(firstName: String, lastName: String)
    case class Team(members: Seq[Person])

    trait BlacklistDao {
      def isBlacklisted(firstName: String, lastName: String): Future[Either[String, Boolean]]
    }

    trait TeamDao {
      def insert(team: Team): Future[Either[String, Unit]]
    }

    def addTeam(team: Team) = {
      // if any of the team member is black listed return error string
      // else insert team
      // any dao opertaion errors(returns Left), stop process, return.
      implicit val ec: ExecutionContext = ExecutionContext.global
      val blacklistDao = mock[BlacklistDao]
      val teamDao = mock[TeamDao]

      val blacklistResults = Future.sequence(team.members.map(m => blacklistDao.isBlacklisted(m.firstName, m.lastName)))
      val aggregateResult = for {
        errors <- blacklistResults.map(_.collect {
          case Left(err) => err})
        isBlacklisted <- blacklistResults.map(_.exists(r => r == Right(true)))
      } yield (errors, isBlacklisted)

      aggregateResult.flatMap {
        case (errors, isBlacklisted) => (errors.isEmpty, isBlacklisted) match {
          case (false, _) => Future.failed(new Exception(errors.mkString(",")))
          case (true, true) => Future.successful("is blacklisted")
          case (true, false) => teamDao.insert(team)
        }
      }
    }
  }
}
