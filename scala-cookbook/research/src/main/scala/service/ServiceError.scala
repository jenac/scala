package service

import db.DatabaseResponse


trait ServiceError {
  def message: String
}

object ServiceError {
  case class GeneralError (message: String) extends ServiceError
  case class DatabaseError(message: String, detail: DatabaseResponse.DbError) extends ServiceError
}

