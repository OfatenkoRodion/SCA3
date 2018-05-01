package module

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import com.github.swagger.akka._
import com.github.swagger.akka.model.Info
import rest.{LanguageRoutes, MetricsRoutes, OrderRoutes, UserRoutes}

import scala.collection.immutable.Set

object SwaggerDocModule extends SwaggerHttpService {

  override val apiClasses: Set[Class[_]] = Set( classOf[LanguageRoutes], classOf[MetricsRoutes], classOf[OrderRoutes], classOf[UserRoutes])
  override val host = System.getenv("SWAGGER_SERVER_HOST") + ":" + System.getenv("SWAGGER_SERVER_PORT")
  override val info = Info(version = "2.0")

  def assets: Route = pathPrefix("swagger") {
    getFromResourceDirectory("swagger") ~ pathSingleSlash(get(redirect("index.html", StatusCodes.PermanentRedirect)))
  }

}
