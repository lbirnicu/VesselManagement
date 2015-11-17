import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.libs.json.JsString
import play.api.test.Helpers._
import play.api.test._

@RunWith(classOf[JUnitRunner])
class AddVesselSpec extends Specification {

  "AddVessel" should {
    "return 404 Bad Request when it receives an invalid request" in new WithApplication {
      val result = route(FakeRequest(POST, "/api/vessels",
        FakeHeaders(List((CONTENT_TYPE, List("application/json")))), JsString("randomJson"))).get

      status(result) should_== BAD_REQUEST
      contentType(result) must beSome("application/json")
      contentAsString(result) should_== """"Invalid json format for type Vessel""""
    }
  }
}
