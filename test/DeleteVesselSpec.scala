import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test.Helpers._
import play.api.test._

@RunWith(classOf[JUnitRunner])
class DeleteVesselSpec extends Specification {

  "DeleteVessel" should {

    val headers = List((CONTENT_TYPE, List("application/json")))

    "successfully delete a vessel" in new WithApplication{

      val result = route(FakeRequest(DELETE, "/api/vessels/randomId", FakeHeaders(headers), "")).get
      status(result) should_== NO_CONTENT
      contentType(result) must beSome("application/json")
    }

    "return 404 Not Found when the vessel with Id doesn't exist" in new WithApplication {
      val result = route(FakeRequest(DELETE, "/api/vessels/randomId", FakeHeaders(headers), "")).get

      status(result) should_== NOT_FOUND
      contentType(result) must beSome("application/json")
      contentAsString(result) should_== """"Cannot find vessel with id: randomId.""""
    }
  }
}
