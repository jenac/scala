package service


import java.rmi.ServerError

import db.Dao
import db.DatabaseResponse.DbError
import model.api.User

class Service(dao: Dao) {
//  def getById(id: String): Either[ServerError, Option[User]] = dao.findById(id) match {
//    case Left(DbError(message)) => ServerError(AAA)
//    case Right()
//  }
}
