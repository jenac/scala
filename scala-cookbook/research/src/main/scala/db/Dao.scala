package db

import java.time.LocalDate

import db.DatabaseResponse.{CollectionResult, DbError, ExecutionResult, SingleResult}

class Dao() {

  def insertOrUpdate(userEntity: UserEntity) : ExecutionResult = ???

  def findById(id: String): SingleResult[UserEntity] = id match {
        case "id-err" => Left(DbError("dao error with name"))
        case "id-none" => Right(None)
        case _ => Right(Some(UserEntity(id = "id-a",
          first="a-first",
          last = "a-last",
          dob = LocalDate.of(1953, 5, 16),
          description = "a-description"
        )))
      }

  def findByFirst(first: String): CollectionResult[UserEntity] = ???
}
