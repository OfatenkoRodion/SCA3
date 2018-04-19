package rest

import javax.ws.rs.Path

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes.{Created, InternalServerError, BadRequest}
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.StrictLogging
import dbGroup.entities.MetricsList
import io.swagger.annotations._
import module._
import rest.entities.{RequestCreateUser, ServerResponse}
import util.{JsonSupport, UserCreationException}

import scala.util.{Failure, Success}

@Path("/")
@Api(value = "/user", produces = "application/json")
class UserRoutes(modules: RoutesHandlerModule with StrictLogging with ActorModule with Configuration) extends Directives with JsonSupport with SprayJsonSupport {

  implicit val system = modules.system
  implicit val materializer = ActorMaterializer()


  def createUser = path("user"){
    post {
      entity(as[RequestCreateUser]) { entity =>
        onComplete(modules.createUser(entity)) {
        case Success(response) => complete(Created, ServerResponse(response))
        case Failure(error: UserCreationException) => complete(BadRequest, error._msg)
        case Failure(ex) =>
          complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
        }
      }
    }
  }

  val userRoutes: Route = createUser

}