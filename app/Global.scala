import play.api.GlobalSettings
import play.api.mvc._

object Global extends WithFilters(CorsFilter()) with GlobalSettings