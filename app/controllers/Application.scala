package controllers

import play.api.mvc._
import play.modules.reactivemongo.MongoController

/**
  * Entry point in the application.
  *
  * Mixes in [[VesselOperations]] trait to have access to exposed CRUD operations
  */
object Application
  extends Controller
  with VesselOperations
  with MongoController {

  def index = Action {
    Ok("Welcome")
  }

  def options(path: String) = Action {
    NoContent.as("application/json")
  }
}