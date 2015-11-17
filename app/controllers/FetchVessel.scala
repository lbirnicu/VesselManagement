package controllers

import controllers.Application._
import models.Vessel._
import models._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.{JsString, JsArray, JsValue, Json}
import play.api.mvc.Action
import reactivemongo.api.Cursor
import reactivemongo.core.actors.Exceptions.PrimaryUnavailableException

import scala.concurrent.Future

trait FetchVessel extends Base {

//  def fetchVessel(name: String) = Action.async {
//
//    val cursor: Cursor[Vessel] = vesselCollection.find(Json.obj("name" -> name)).cursor[Vessel]
//
//    val futureVesselList: Future[List[Vessel]] = cursor.collect[List]()
//
//    futureVesselList.map { vessels =>
//      vessels.headOption.map{ vessel =>
//        Ok(vesselFormat.writes(vessel))
//      }.getOrElse(
//        NotFound(JsString("No vessel with this name could be found"))
//      )
//    }.recover({
//      case PrimaryUnavailableException =>
//        InternalServerError(""""Unexpected error occurred.Please try again later!"""")
//          .as("application/json")
//    })
//  }

  def fetchVessel(id: String) = Action.async {

    val cursor: Cursor[Vessel] = vesselCollection.find(Json.obj("_id" -> id)).cursor[Vessel]

    val futureVesselList: Future[List[Vessel]] = cursor.collect[List]()

    futureVesselList.map { vessels =>
      vessels.headOption.map{ vessel =>
        Ok(vesselFormat.writes(vessel))
      }.getOrElse(
        NotFound(JsString("No vessel with this name could be found"))
      )
    }.recover({
      case PrimaryUnavailableException =>
        InternalServerError(""""Unexpected error occurred.Please try again later!"""")
          .as("application/json")
    })
  }

  def fetchVessels = Action.async {

    val cursor: Cursor[Vessel] = vesselCollection.find(Json.obj()).cursor[Vessel]

    val futureVessels: Future[JsValue] = cursor.collect[List]().map { vessels =>
      val vesselsJsValues = vessels map { v => vesselFormat.writes(v)}
      JsArray(vesselsJsValues)
    }

    futureVessels.map { vessels =>
      Ok(vessels).as("application/json")
    }.recover({
      case PrimaryUnavailableException =>
        InternalServerError(""""Unexpected error occurred.Please try again later!"""")
          .as("application/json")
    })
  }
}
