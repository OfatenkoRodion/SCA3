
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.RouteConcatenation
import akka.stream.ActorMaterializer
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import com.typesafe.scalalogging.StrictLogging
import dbGroup.{DBMigrationImpl, DBModuleImpl}
import module._
import rest.{LanguageRoutes, ZoomRoutes}

object Main extends App with RouteConcatenation with StrictLogging {


  val modules = new ConfigurationModuleImpl with ActorModuleImpl with RoutesHandlerModuleImpl with DBModuleImpl with DBMigrationImpl

  implicit val system = modules.system
  implicit val materializer = ActorMaterializer()
  implicit val ec = modules.system.dispatcher

  val bindingFuture = Http().bindAndHandle(
    cors()(
      new ZoomRoutes(modules).zoomRoutes~
        new LanguageRoutes(modules).languageRoutes~
        SwaggerDocModule.assets ~
        SwaggerDocModule.routes
        )
    , modules.config.getString("http.host"), modules.config.getInt("http.port")
  )

  bindingFuture
    .map(binding => logger.info(s"Server bound to ${binding.localAddress}"))
    .recover { case ex =>
      logger.error(s"Server could not bind to ${modules.config.getString("http.host")}:${modules.config.getInt("http.port")}", ex.getMessage)
      system.terminate()
    }
}