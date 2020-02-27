package json

import java.time.OffsetDateTime

import com.rallyhealth.weepickle.v1.WeePickle
import com.rallyhealth.weepickle.v1.implicits.dropDefault
import play.api.libs.json.Json

case class Resident(name: String,
                    age: Int,
                    @dropDefault role: Option[String] = None,
                    data: Map[String, String],
                    start: OffsetDateTime
                   )
object  Resident {
  implicit val residentFormat = Json.format[Resident]
  implicit val residentRw = WeePickle.macroFromTo[Resident]
}