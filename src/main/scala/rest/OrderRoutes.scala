package rest

import javax.ws.rs.Path
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes.{BadRequest, Created, InternalServerError}
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.StrictLogging
import dbGroup.entities.LanguageEntity
import io.swagger.annotations._
import module._
import rest.entities.{RequestCreateOrder, ServerResponse}
import util.{JsonSupport, UserCreationException}

import scala.util.{Failure, Success}

@Path("/order")
@Api(value = "/order", produces = "application/json")
class OrderRoutes(modules: RoutesHandlerModule with StrictLogging with ActorModule with Configuration) extends Directives with JsonSupport with SprayJsonSupport {

  implicit val system = modules.system
  implicit val materializer = ActorMaterializer()


  @Path("/add")
  @ApiOperation(value = "Add order", notes = "", nickname = "", httpMethod = "POST", produces = "application/json")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "body", value = "RequestCreateOrder", required = true,
      dataType = "rest.entities.RequestCreateOrder", paramType = "body")

  ))
  @ApiResponses(Array(
    new ApiResponse(code = 201, message = "ServerResponse entity", response = classOf[ServerResponse])
  ))
  def createOrder = path("order" / "add"){
    post {
      entity(as[RequestCreateOrder]) { entity =>
        onComplete(modules.createOrder(entity)) {
        case Success(response) => complete(Created, ServerResponse(response))
        case Failure(error: UserCreationException) => complete(BadRequest, error._msg)
        case Failure(ex) =>
          complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
        }
      }
    }
  }

  @Path("/{orderId}/start")
  @ApiOperation(value = "Add order", notes = "", nickname = "", httpMethod = "POST", produces = "application/json")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "orderId", value = "orderId", required = true, dataType = "string", paramType = "path")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 201, message = "ServerResponse entity", response = classOf[ServerResponse])
  ))
  def startOrder = path("order" / LongNumber / "start"){ orderId =>
    post {
        onComplete(modules.startOrder(orderId)) {
          case Success(response) => complete(Created, ServerResponse(response))
          case Failure(error: UserCreationException) => complete(BadRequest, error._msg)
          case Failure(ex) =>
            complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
        }
      }
  }

  val orderRoutes: Route = createOrder ~startOrder

}