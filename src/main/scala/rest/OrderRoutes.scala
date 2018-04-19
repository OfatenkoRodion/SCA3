package rest

import javax.ws.rs.Path

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes.{BadRequest, Created, InternalServerError}
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.StrictLogging
import io.swagger.annotations._
import module._
import rest.entities.{RequestCreateOrder, ServerResponse}
import util.{JsonSupport, UserCreationException}

import scala.util.{Failure, Success}

@Path("/")
@Api(value = "/order", produces = "application/json")
class OrderRoutes(modules: RoutesHandlerModule with StrictLogging with ActorModule with Configuration) extends Directives with JsonSupport with SprayJsonSupport {

  implicit val system = modules.system
  implicit val materializer = ActorMaterializer()


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

  val orderRoutes: Route = createOrder

}