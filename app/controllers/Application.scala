package controllers

import play.api.mvc._
import play.modules.reactivemongo.MongoController

object Application
  extends Controller
  with AddVessel
  with FetchVessel
  with UpdateVessel
  with DeleteVessel
  with MongoController {

  def index = Action {
    Ok("Welcome")
  }

  def options(path: String) = Action {
    Ok("")
  }
}