package controllers

import controllers.Application._
import models.Vessel._
import models._
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.mvc.Action
import play.api.mvc.BodyParsers.parse

import scala.concurrent.Future

trait UpdateVessel extends Base {

  def updateVessel(id: String) = Action.async(parse.json) { request =>
    request.body.validate[Vessel].map { vessel =>
      vesselCollection.update(Json.obj("_id" -> id), vessel).map { lastError =>
            Logger.debug(s"Successfully inserted with LastError: $lastError")
            Ok( s""""The vessel was successfully updated"""").as("application/json")
        }.recover({case _ => InternalServerError})
    }.getOrElse(
      Future.successful(
        BadRequest(""""Invalid json format for type Vessel"""")
        .as("application/json")
      )
    )
  }
}
