package controllers

import controllers.Application._
import models.Vessel._
import models._
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.Action
import play.api.mvc.BodyParsers.parse

import scala.concurrent.Future

trait AddVessel extends Base {

  def addVessel() = Action.async(parse.json) { request =>
    request.body.validate[Vessel].map { vessel =>
      vesselCollection.insert(vessel).map { lastError =>
        Logger.debug(s"Successfully inserted with LastError: $lastError")
        Created(s""""The vessel was successfully created"""").as("application/json")
      }
    }.getOrElse(
      Future.successful(
        BadRequest(""""Invalid json format for type Vessel"""")
        .as("application/json")
      )
    )
  }
}
