import models.Vessel._
import models.{Coordinates, Dimensions, Vessel}
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test.Helpers._
import play.api.test._

@RunWith(classOf[JUnitRunner])
class FetchVesselSpec extends Specification {

  "FetchVessel" should {

    val headers = List((CONTENT_TYPE, List("application/json")))
    val dimension = Dimensions(30, 200, 14)
    val coordinates = Coordinates(9.00, 18.023)
    val vesselObj = Vessel("randomId", "vesselNameUpdated", dimension, coordinates)

    "successfully fetch a vessel" in new WithApplication {
      val result =
        route(FakeRequest(GET, "/api/vessels/randomId", FakeHeaders(headers), "")).get

      status(result) should_== OK
      contentType(result) must beSome("application/json")
      contentAsString(result) shouldEqual vesselFormat.writes(vesselObj).toString()
    }

    "return 404 NoT Found when tries to get an unknown id" in new WithApplication {
      val result =
        route(FakeRequest(GET, "/api/vessels/unknownId", FakeHeaders(headers), "")).get

      status(result) should_== NOT_FOUND
      contentType(result) must beSome("application/json")
      contentAsString(result) should_== """"Cannot find vessel with id: unknownId.""""
    }
  }
}
