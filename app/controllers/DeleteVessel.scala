package controllers

import controllers.Application._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.mvc.Action

trait DeleteVessel extends Base {

  def deleteVessel(name: String) = Action.async {
    vesselCollection.remove(Json.obj("name" -> name)).map{ le =>
       NoContent.as("application/json")
      }.recover({
      case _ => InternalServerError.as("application/json")
    })
  }
}
