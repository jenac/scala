package json

import java.time.{OffsetDateTime, ZoneId, ZoneOffset, ZonedDateTime}

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import play.api.libs.json._
import com.rallyhealth.weejson.v1.jackson.{FromJson, ToJson, ToPrettyJson}
import com.rallyhealth.weepickle.v1.WeePickle.{FromScala, ToScala}

class ResidentSpec extends AnyFlatSpec with Matchers {
  val start = OffsetDateTime.of(2001, 3, 16, 13, 35, 59, 1234, ZoneOffset.ofHours(6))
    val jakeObject = Resident("Jake", 36, Some("CTO"), Map("state" -> "MN", "country" -> "USA"), start)
    val jakeString = """{"name":"Jake","age":36,"role":"CTO","data":{"state":"MN","country":"USA"},"start":"2001-03-16T13:35:59.000001234+06:00"}"""
    val timObject = Resident("Tim", 42, None,
      Map("language" -> "C#", "database" -> "SQL Server", "ide" -> "Visual Studio"), start)
    val timString = """{"name":"Tim","age":42,"data":{"language":"C#","database":"SQL Server","ide":"Visual Studio"},"start":"2001-03-16T13:35:59.000001234+06:00"}"""
  "PlayJson" should "serialize Resident to json" in {
    val jakeJsValue = Json.toJson(jakeObject)
    jakeJsValue.toString shouldBe jakeString

    val timeJsValue = Json.toJson(timObject)
    timeJsValue.toString shouldBe timString
  }

  "PlayJson" should "deserialize Resident from json" in {
    val jakeJsValue = Json.parse(jakeString)
    val jake = jakeJsValue.as[Resident]
    jake shouldBe jakeObject

    val timJsValue = Json.parse(timString)
    val tim = timJsValue.as[Resident]
    tim shouldBe timObject

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

  "WeePickle" should "works" in {
    FromJson("[1,2,3]").transform(ToScala[List[Int]]) shouldBe List(1, 2, 3)
    FromScala(List(1,2,3)).transform(ToJson.string) shouldBe "[1,2,3]"
    FromScala(List(1,2,3)).transform(ToPrettyJson.string) shouldBe """[
                                                                     |  1,
                                                                     |  2,
                                                                     |  3
                                                                     |]""".stripMargin
  }

  //WeePickle does not support ZonedDateTime
  "WeePickle" should "serialize Resident to json" in {
    FromScala(jakeObject).transform(ToJson.string) shouldBe jakeString
    FromScala(timObject).transform(ToJson.string) shouldBe timString
  }

  "WeePickle" should "deserialize Resident from json" in {
    FromJson(jakeString).transform(ToScala[Resident]) shouldBe jakeObject
    FromJson(timString).transform(ToScala[Resident]) shouldBe timObject
  }

}
