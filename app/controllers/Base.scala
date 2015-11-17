package controllers

import controllers.Application._
import play.modules.reactivemongo.json.collection.JSONCollection

trait Base {
  def vesselCollection: JSONCollection = db.collection[JSONCollection]("vessels")
}
