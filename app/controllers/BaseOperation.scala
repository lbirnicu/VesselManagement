package controllers

import controllers.Application._
import play.api.Logger
import play.api.mvc.Result
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.core.errors.ReactiveMongoException

import scala.concurrent.Future

/**
  * Defines the database collection and http responses that are reuses across operations
  */
trait BaseOperation {

  def vesselCollection: JSONCollection = db.collection[JSONCollection]("vessels")

  val InvalidJsonMessage = """"Invalid json format for type Vessel""""
  val UnexpectedErrorMessage = """"Unexpected error!""""

  def badRequest() = {
    Logger.debug("Invalid json format for type Vessel")
    Future.successful(BadRequest(InvalidJsonMessage).as("application/json"))
  }

  def notFound(id: String, operation: String) = {
    Logger.debug(s"Cannot $operation, the vessel with id: $id doesn't exist.")
    NotFound(s""""Cannot find vessel with id: $id."""").as("application/json")
  }

  def recoverFromMongoException: PartialFunction[Throwable, Result] = {
    case f: ReactiveMongoException =>
      Logger.error("ReactiveMongo exception " + f)
      InternalServerError(UnexpectedErrorMessage).as("application/json")
  }
}
