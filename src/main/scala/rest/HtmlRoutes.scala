package rest

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{Directives, Route}


class HtmlRoutes(system: ActorSystem) extends Directives {

  def indexPage: Route = pathPrefix("app") {
    getFromResourceDirectory("html") ~ pathSingleSlash(get(redirect("index.html", StatusCodes.PermanentRedirect)))
  }

  def css: Route = pathPrefix("css") {
    getFromResourceDirectory("css")
  }


  def js: Route = pathPrefix("js") {
    getFromResourceDirectory("js")
  }

  val routes: Route = indexPage ~ css ~ js

}

