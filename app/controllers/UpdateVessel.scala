package controllers

import controllers.Application._
import models.Vessel._
import models._
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.mvc.Action
import play.api.mvc.BodyParsers.parse
import reactivemongo.core.commands.LastError

/**
  * Defines the operation for updating a vessel
  */
trait UpdateVessel extends BaseOperation {

  /**
    * Handles the http request for updating a vessel.
    *
    * Updates the vessel with `id` if that vessel can be found in the database collection.
    * @param id the `id` of the vessel to be updated
    * @return an `Action` that returns a `Future[Result]`.
    *         Possible results are:
    *           `Ok`                  -> vessel successfully updated
    *           `NotFound`            -> the vessel with `id` doesn't exist in db collection
    *           `InternalServerError` -> unexpected errors from MongoDB
    */
  def updateVessel(id: String) = Action.async(parse.json) { request =>
    request.body.validate[Vessel]
      .map(vessel => update(id, vessel))
      .getOrElse(badRequest())
  }

  private def update(id: String, vessel: Vessel) =
    vesselCollection.update(Json.obj("_id" -> id), vessel)
      .map({
        case le: LastError if le.updated == 0 =>
          notFound(id, "update")
        case _ =>
          Logger.info(s"The vessel with id: $id successfully updated.")
          Ok(s""""The vessel with id: $id was successfully updated."""").as("application/json")
      })
      .recover(recoverFromMongoException)
}
