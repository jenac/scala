package json

import play.api.libs.json.Json

case class Resident(name: String,
                    age: Int,
                    role: Option[String],
                    data: Map[String, String])
object  Resident {
  implicit val residentFormat = Json.format[Resident]
}