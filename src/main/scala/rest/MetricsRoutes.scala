package rest

import javax.ws.rs.Path

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes.{Created, InternalServerError}
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.StrictLogging
import dbGroup.entities.{LanguageEntity, LanguagesList, MetricsList}
import io.swagger.annotations._
import module._
import util.JsonSupport

import scala.util.{Failure, Success}

@Path("/metrics")
@Api(value = "/metrics", produces = "application/json")
class MetricsRoutes(modules: RoutesHandlerModule with StrictLogging with ActorModule with Configuration) extends Directives with JsonSupport with SprayJsonSupport {

  implicit val system = modules.system
  implicit val materializer = ActorMaterializer()


  @Path("/list/{language}")
  @ApiOperation(value = "Get metrics list", notes = "", nickname = "", httpMethod = "GET", produces = "application/json")
  @ApiResponses(Array(
    new ApiResponse(code = 201, message = "MetricsEntity entitys list", response = classOf[MetricsList])
  ))
  def metricsList = path("metrics" / "list" / Segment) { language=>
    get {
      onComplete(modules.getMetricsList(language)) {
        case Success(list) => complete(Created, MetricsList(list))
        case Failure(ex) =>
          complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
      }
    }
  }

  val metricsRoutes: Route = metricsList


}