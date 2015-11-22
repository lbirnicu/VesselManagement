package controllers

import controllers.Application._
import models.Vessel._
import models._
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.Action
import play.api.mvc.BodyParsers.parse

/**
  * Defines the operation for adding a new vessel
  */
trait AddVessel extends BaseOperation {

  /**
    * Handles the http requests for adding a new vessel.
    *
    * Inserts a new vessel in the database collection if the
    * received request body has a valid [[Vessel]] format.
    *
    * @return an `Action` that returns a `Future[Result]`.
    *         Possible results are:
    *           `Created`             -> vessel successfully added
    *           `BadRequest`          -> the request body cannot be parsed to a Vessel
    *           `InternalServerError` -> unexpected errors from MongoDB
    */
  def addVessel() = Action.async(parse.json) { request =>
    request.body.validate[Vessel]
      .map(vessel => insert(vessel))
      .getOrElse(badRequest())
  }

  private def insert(vessel: Vessel) = vesselCollection.insert(vessel)
    .map( _ => {
      Logger.info(s"The vessel with id: ${vessel._id} was successfully inserted.")
      Created(s""""The vessel with id: ${vessel._id} was successfully created."""").as("application/json")
    })
    .recover(recoverFromMongoException)
}
