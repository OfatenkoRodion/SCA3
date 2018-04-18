package rest

import javax.ws.rs.Path

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.ActorMaterializer
import module._
import util.JsonSupport
import com.typesafe.scalalogging.StrictLogging
import dbGroup.entities.LanguageEntity
import io.swagger.annotations._

import scala.util.{Failure, Success}

@Path("/")
@Api(value = "/test", produces = "application/json")
class ZoomRoutes(modules: RoutesHandlerModule with StrictLogging with ActorModule with Configuration) extends Directives with JsonSupport with SprayJsonSupport {

  implicit val system = modules.system
  implicit val materializer = ActorMaterializer()


  def test= path("test" ) {
    get {

      val temp="<!DOCTYPE html PUBLIC \\\"-//IETF//DTD HTML 2.0//EN\\\">\\n<HTML>\\n   <HEAD>\\n      <TITLE>\\n         A Small Hello \\n      </TITLE>\\n   </HEAD>\\n<BODY>\\n   <H1>Hi</H1>\\n   <P>This is very minimal \\\"hello world\\\" HTML document.</P> \\n</BODY>\\n</HTML>"
      complete(HttpResponse(entity=HttpEntity(ContentTypes.`text/html(UTF-8)`, temp)))
    }
  }
  val zoomRoutes: Route = test


}