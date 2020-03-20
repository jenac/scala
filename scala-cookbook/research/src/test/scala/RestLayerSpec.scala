import java.rmi.ServerError
import java.time.{LocalDate, Period}

import db.DatabaseResponse.{DbError, SingleResult}
import db.UserEntity
import model.api.User
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import service.ServiceError.DatabaseError

import scala.concurrent.{ExecutionContext, Future}

class RestLayerSpec extends AnyFlatSpec with Matchers{
  it should "aaa" in {
    def mapUser(userEntity: Option[UserEntity]) : Option[User] = {
      def mapImpl(userEntity: UserEntity): User = User(fullName = s"${userEntity.first}, ${userEntity.last}",
        age = Period.between(userEntity.dob, LocalDate.now).getYears,
        description = userEntity.description
      )
      userEntity match {
        case Some(entity) => Some(mapImpl(entity))
        case _ => None
      }

    }
    val dateOfBirth = LocalDate.of(19720,1,1)
    val s1: SingleResult[UserEntity] =
      Right(Some(UserEntity(id="a-id",
        first = "a-first",
        last="a-last",
        dob=dateOfBirth,
        description="a-desc")))
    val m1 = s1.fold(_ => DatabaseError("database error", _), mapUser(_))

    val s2: SingleResult[UserEntity] = Left(DbError("Something"))
    val m2 = s2.fold(DatabaseError("a", _), mapUser(_))

    val s3: SingleResult[UserEntity] = Right(None)
    val m3 = s3.fold(_ => DatabaseError("database error", _), mapUser(_))

    implicit val ec: ExecutionContext = ExecutionContext.global
    val f1: Future[SingleResult[UserEntity]] = Future.successful(s1)

    val fv1 = f1.map {
      case Left(error) => DatabaseError("aa", error)
      case Right(maybe) => mapUser(maybe)
    }

    val s= "what?"
  }
}
