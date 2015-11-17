package models

import play.api.libs.json.Json

case class Dimensions(width: Double, length: Double, draft: Double)
case class Coordinates(latitude: Double, longitude: Double)
case class Vessel(_id: String, name: String, dimension: Dimensions, coordinates: Coordinates)

object Coordinates {
  implicit val coordinatesFormat = Json.format[Coordinates]
}

object Dimensions {
  implicit val dimensionsFormat = Json.format[Dimensions]
}

object Vessel {
  implicit val vesselFormat = Json.format[Vessel]
}