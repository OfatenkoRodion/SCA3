package rest

import javax.ws.rs.Path

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes.{Created, InternalServerError}
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.StrictLogging
import dbGroup.entities.{LanguageEntity, LanguagesList}
import io.swagger.annotations._
import module._
import util.JsonSupport

import scala.util.{Failure, Success}

@Path("/")
@Api(value = "/language", produces = "application/json")
class LanguageRoutes(modules: RoutesHandlerModule with StrictLogging with ActorModule with Configuration) extends Directives with JsonSupport with SprayJsonSupport {

  implicit val system = modules.system
  implicit val materializer = ActorMaterializer()


  @Path("/language/add/{language}")
  @ApiOperation(value = "Add language", notes = "", nickname = "", httpMethod = "POST", produces = "application/json")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "language", value = "language", required = true, dataType = "string", paramType = "path")))
  @ApiResponses(Array(
    new ApiResponse(code = 201, message = "LanguageEntity entity", response = classOf[LanguageEntity])
  ))
  def addLanguage = path("language" / "add" / Segment) { language =>
    post {
        onComplete(modules.addLanguage(language)) {
          case Success(entity) => complete(Created, entity)
          case Failure(ex) =>
            complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
        }
    }
  }

  @Path("/language/list")
  @ApiOperation(value = "Get language list", notes = "", nickname = "", httpMethod = "GET", produces = "application/json")
  @ApiResponses(Array(
    new ApiResponse(code = 201, message = "LanguageEntity entitys list", response = classOf[LanguagesList])
  ))
  def languageList = path("language" / "list") {
    get {
      onComplete(modules.getLanguageList) {
        case Success(list) => complete(Created, LanguagesList(list))
        case Failure(ex) =>
          complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
      }
    }
  }

  val languageRoutes: Route = addLanguage ~languageList


}