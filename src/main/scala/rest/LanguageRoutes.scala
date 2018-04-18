package rest

import javax.ws.rs.Path

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes.{Created, InternalServerError}
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.StrictLogging
import dbGroup.entities.LanguageEntity
import io.swagger.annotations._
import module._
import util.JsonSupport

import scala.util.{Failure, Success}

class LanguageRoutes(modules: RoutesHandlerModule with StrictLogging with ActorModule with Configuration) extends Directives with JsonSupport with SprayJsonSupport {

  implicit val system = modules.system
  implicit val materializer = ActorMaterializer()

  def addLanguage = path("language" / "add" / Segment) { language =>
    post {
        onComplete(modules.addLanguage(language)) {
          case Success(entity) => complete(Created, entity)
          case Failure(ex) =>
            complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
        }
    }
  }
  val languageRoutes: Route = addLanguage


}