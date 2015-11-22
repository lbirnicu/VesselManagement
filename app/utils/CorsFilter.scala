package utils

import play.api.mvc.{Filter, RequestHeader, Result}

/**
  * Defines a filter for the application to allow cross-origin HTTP requests
  */
case object CorsFilter extends Filter {
  import scala.concurrent._
  import ExecutionContext.Implicits.global

  /**
    * For each result, adds the `Access-Control-Allow-Methods`,
    * `Access-Control-Allow-Origin` and `Access-Control-Allow-Headers` headers
    */
  def apply(f: (RequestHeader) => Future[Result])(request: RequestHeader): Future[Result] =
    f(request).map(_.withHeaders(
      "Access-Control-Allow-Methods" -> "DELETE, GET, POST, PUT, OPTIONS",
      "Access-Control-Allow-Origin" -> "*",
      "Access-Control-Allow-Headers" -> "Accept, Origin, Content-type, X-Json, X-Prototype-Version, X-Requested-With"
    ))
}