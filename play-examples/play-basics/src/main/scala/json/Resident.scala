package json

import play.api.libs.json.Json

case class Resident(name: String,
                    age: Int,
                    role: Option[String],
                    data: Map[String, String])
object  Resident {
  implicit val residentWrites = Json.writes[Resident]
  implicit val residentReads = Json.reads[Resident]
}