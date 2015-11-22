package controllers

import controllers.Application._
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.mvc.Action
import reactivemongo.core.commands.LastError

/**
  * Defines the operation for deleting a vessel
  */
trait DeleteVessel extends BaseOperation {

  /**
    * Handles the http requests for deleting a vessel.
    *
    * Removes the vessel with `id` if that vessel can be found in the database collection.
    * @param id the `id` of the vessel to be removed
    * @return an `Action` that returns a `Future[Result]`.
    *         Possible results are:
    *           `NoContent`           -> vessel successfully removed
    *           `NotFound`            -> the vessel with `id` doesn't exist in db collection
    *           `InternalServerError` -> unexpected errors from MongoDB
    */
  def deleteVessel(id: String) = Action.async {
    vesselCollection.remove(Json.obj("_id" -> id))
      .map({
        case le: LastError if le.updated == 0 =>
          notFound(id, "remove")
        case _ =>
          Logger.info(s"Successfully deleted the vessel with id: $id.")
          NoContent.as("application/json")
      })
      .recover(recoverFromMongoException)
  }
}
