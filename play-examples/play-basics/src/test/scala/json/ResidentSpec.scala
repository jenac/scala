package json

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import play.api.libs.json._

class ResidentSpec extends AnyFlatSpec with Matchers {
  "Resident" should "serialize to json" in {
    val jake = Resident("Jake", 36, Some("CTO"), Map("state" -> "MN", "country" -> "USA"))
    val jakeJsValue = Json.toJson(jake)
    jakeJsValue.toString shouldBe """{"name":"Jake","age":36,"role":"CTO","data":{"state":"MN","country":"USA"}}"""

    val tim = Resident("Tim", 42, None, Map("language" -> "C#", "database" -> "SQL Server", "ide" -> "Visual Studio"))
    val timeJsValue = Json.toJson(tim)
    timeJsValue.toString shouldBe """{"name":"Tim","age":42,"data":{"language":"C#","database":"SQL Server","ide":"Visual Studio"}}"""
  }

  "Resident" should "deserialize from json" in {
    val jakeString = """{"name":"Jake","age":36,"role":"CTO","data":{"state":"MN","country":"USA"}}"""
    val jakeJsValue = Json.parse(jakeString)
    val jake = jakeJsValue.as[Resident]
    jake shouldBe Resident("Jake", 36, Some("CTO"), Map("state" -> "MN", "country" -> "USA"))

    val timString = """{"name":"Tim","age":42,"data":{"language":"C#","database":"SQL Server","ide":"Visual Studio"}}"""
    val timJsValue = Json.parse(timString)
    val tim = timJsValue.as[Resident]
    tim shouldBe Resident("Tim", 42, None, Map("language" -> "C#", "database" -> "SQL Server", "ide" -> "Visual Studio"))

    val badString = """{ how are you?"""
    //    val badJsValue = Json.parse(badString) //this will throw exception

    val nonResidentString = """{"hello": "world" }"""
    val nonResidentJsValue = Json.parse(nonResidentString)
    //    val nonResident = nonResidentJsValue.as[Resident] //this will throw exception

    def residentFromJsValue(input: JsValue): Option[Resident] = Json
      .fromJson[Resident](input) match {
        case JsSuccess(r: Resident, p: JsPath) => Some(r)
        case e@JsError(_) => {
          println("Errors: " + JsError.toJson(e).toString())
          None
        }
    }
    residentFromJsValue(nonResidentJsValue) shouldBe None
    residentFromJsValue(timJsValue).isDefined shouldBe true


  }

}
