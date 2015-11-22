package models

import play.api.libs.json.Json


case class Dimensions(width: Double, length: Double, draft: Double)
case class Coordinates(latitude: Double, longitude: Double)

/**
  * Defines the model for a vessel
  *
  * @param _id a unique vessel id that will be used as the `_id` of the document that holds this vessel
  *            in the db collection
  * @param name the vessel name
  * @param dimension the `width`, `length` and `draft` dimensions of the vessel
  * @param coordinates the `latitude` and `longitude` coordinates of the vessel
  */
case class Vessel(_id: String, name: String, dimension: Dimensions, coordinates: Coordinates)

/**
  * Holds the implicit json formats for Vessel, Dimensions and Coordinates
  */
object Vessel {
  implicit val dimensionsFormat = Json.format[Dimensions]
  implicit val coordinatesFormat = Json.format[Coordinates]
  implicit val vesselFormat = Json.format[Vessel]
}