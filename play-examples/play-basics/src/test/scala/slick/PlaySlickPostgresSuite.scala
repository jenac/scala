package slick

import com.dimafeng.testcontainers.{ForAllTestContainer, PostgreSQLContainer}
import org.mockito.scalatest.MockitoSugar
import org.scalatest.{Assertion, Suite}
import org.scalatestplus.play.AppProvider
import play.api.db.Database
import play.api.db.evolutions.Evolutions
import com.github.tminglei.slickpg.ExPostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

trait PlaySlickPostgresSuite extends Suite
  with AppProvider
  with ForAllTestContainer
  with MockitoSugar {

  override def container: PostgreSQLContainer

  def applyEvolutions(): Unit = {
    withDatabase { database =>
      Evolutions.applyEvolutions(database, autocommit = false)
    }
  }

  def unapplyEvolutions(): Unit = {
    withDatabase { database =>
      Evolutions.cleanupEvolutions(database, autocommit = false)
    }
  }

  def withEvolutions(assertionFun: () => Assertion): Assertion = {
    applyEvolutions()
    val assertion = assertionFun.apply()
    unapplyEvolutions()
    assertion
  }

  def withEvolutions(futureAssertionFun: () => Future[Assertion])(implicit ec: ExecutionContext): Future[Assertion] = {
    applyEvolutions()
    futureAssertionFun
      .apply()
      .map {
        assertion =>
          unapplyEvolutions()
          assertion
      }
  }

  private def withDatabase(block: (Database) => Unit) = {
    val database:Database = Database.forURL(container.jdbcUrl).b
//    (
//      driver = "org.postgresql.Driver",
//      url = container.jdbcUrl,
//      name = "default",
//      config = Map(
//        "username" -> container.username,
//        "password" -> container.password
//      )
//    )
    try {
      val result = block(database)
      database.shutdown()
      result
    } catch {
      case NonFatal(e) =>
        database.shutdown()
        throw e
    }
  }
}
