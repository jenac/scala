package db

trait DatabaseResponse {
  def message: String
}

object DatabaseResponse {
  case class DbSuccess (message: String) extends DatabaseResponse
  case class DbError(message: String) extends DatabaseResponse
  type ExecutionResult = Either[DbError, DbSuccess]
  type SingleResult[A] = Either[DbError, Option[A]]
  type CollectionResult[A] = Either[DbError, Seq[A]]
}
