import models.Vessel._
import models.{Coordinates, Dimensions, Vessel}
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.libs.json.JsString
import play.api.test.Helpers._
import play.api.test._

@RunWith(classOf[JUnitRunner])
class UpdateVesselSpec extends Specification {

  "UpdateVessel" should {

    val headers = List((CONTENT_TYPE, List("application/json")))

    val dimension = Dimensions(30, 200, 14)
    val coordinates = Coordinates(9.00, 18.023)
    val vesselObj = Vessel("randomId", "vesselNameUpdated", dimension, coordinates)

    "successfully update an existent vessel" in new WithApplication{

      val result =
        route(FakeRequest(PUT, "/api/vessels/randomId", FakeHeaders(headers), vesselFormat.writes(vesselObj))).get

      status(result) should_== OK
      contentType(result) must beSome("application/json")
      contentAsString(result) should_== """"The vessel with id: randomId was successfully updated.""""
    }

    "return 400 Bad Request when it receives an invalid request" in new WithApplication {
      val result =
        route(FakeRequest(PUT, "/api/vessels/randomId", FakeHeaders(headers), JsString("randomJson"))).get

      status(result) should_== BAD_REQUEST
      contentType(result) must beSome("application/json")
      contentAsString(result) should_== """"Invalid json format for type Vessel""""
    }

    "return 404 Not Found when tries to update an unknown vessel" in new WithApplication {
      val result =
        route(FakeRequest(PUT, "/api/vessels/unknownId", FakeHeaders(headers), vesselFormat.writes(vesselObj))).get

      status(result) should_== NOT_FOUND
      contentType(result) must beSome("application/json")
      contentAsString(result) should_== """"Cannot find vessel with id: unknownId.""""
    }
  }
}
