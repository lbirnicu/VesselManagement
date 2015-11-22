import models.{Coordinates, Dimensions, Vessel}
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.libs.json.JsString
import play.api.test.Helpers._
import play.api.test._
import models.Vessel._

@RunWith(classOf[JUnitRunner])
class AddVesselSpec extends Specification {

  "AddVessel" should {

    val headers = List((CONTENT_TYPE, List("application/json")))

    "successfully add a new vessel" in new WithApplication {
      val dimension = Dimensions(30, 200, 14)
      val coordinates = Coordinates(9.00, 18.023)
      val vesselObj = Vessel("randomId", "vesselName", dimension, coordinates)

      val result =
        route(FakeRequest(POST, "/api/vessels", FakeHeaders(headers), vesselFormat.writes(vesselObj))).get

      status(result) should_== CREATED
      contentType(result) must beSome("application/json")
      contentAsString(result) should_== """"The vessel with id: randomId was successfully created.""""
    }

    "return 400 Bad Request when it receives an invalid request" in new WithApplication {
      val result =
        route(FakeRequest(POST, "/api/vessels", FakeHeaders(headers), JsString("randomJson"))).get

      status(result) should_== BAD_REQUEST
      contentType(result) must beSome("application/json")
      contentAsString(result) should_== """"Invalid json format for type Vessel""""
    }
  }
}
