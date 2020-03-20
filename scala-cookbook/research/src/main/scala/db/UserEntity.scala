package db

import java.time.LocalDate

case class UserEntity (id: String, first: String, last: String, dob: LocalDate, description: String)
