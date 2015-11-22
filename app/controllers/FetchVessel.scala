package controllers

import controllers.Application._
import models.Vessel._
import models._
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.{JsArray, Json}
import play.api.mvc.Action

import scala.concurrent.Future

/**
  * Defines the operations for fetching one or all lists
  */
trait FetchVessel extends BaseOperation {

  /**
    * Handles the http request for fetching the vessel with `id`
    *
    * @param id the `id` of the vessel to be fetched
    * @return an `Action` that returns a `Future[Result]`.
    *         Possible results are:
    *           `OK`                  -> vessel successfully fetch, returned in the response body
    *           `NotFound`            -> the vessel with `id` doesn't exist in db collection
    *           `InternalServerError` -> unexpected errors from MongoDB
    */
  def fetchVessel(id: String) = Action.async {

    val futureVesselList: Future[List[Vessel]] = vesselCollection
      .find(Json.obj("_id" -> id)).cursor[Vessel]
      .collect[List]()

    futureVesselList
      .map(vessels => vessels.headOption
        .map(vessel => {
          Logger.info(s"The vessel with id: $id was successfully fetched.")
          Ok(vesselFormat.writes(vessel))
        })
        .getOrElse(notFound(id, "fetch")))
      .recover(recoverFromMongoException)
  }

  /**
    * Handles the requests for fetching all the vessel from the database collection
    *
    * @return an `Action` that returns a `Future[Result]`.
    *         Possible results are:
    *           `OK`                  -> vessels successfully fetch, returned in the response body
    *           `InternalServerError` -> unexpected errors from MongoDB
    */
  def fetchVessels = Action.async {
    vesselCollection.find(Json.obj()).cursor[Vessel].collect[List]()
      .map(vessels => {
        Logger.info(s"The vessels list was successfully fetched.")
        Ok(JsArray(vessels.map(vesselFormat.writes)))
      })
      .recover(recoverFromMongoException)
  }
}
