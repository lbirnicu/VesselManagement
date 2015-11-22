import play.api.GlobalSettings
import play.api.mvc._
import utils.CorsFilter

object Global extends WithFilters(CorsFilter) with GlobalSettings