package util

import dbGroup.entities._
import de.knutwalker.akka.http.support.CirceHttpSupport
import de.knutwalker.akka.stream.support.CirceStreamSupport
import rest.entities.{RequestCreateOrder, RequestCreateUser, ServerResponse}
import spray.json.DefaultJsonProtocol

trait JsonSupport extends DefaultJsonProtocol with CirceHttpSupport with CirceStreamSupport {

  implicit val languageEntityFormat = jsonFormat2(LanguageEntity)
  implicit val languagesListFormat = jsonFormat1(LanguagesList)

  implicit val metricsEntityFormat = jsonFormat4(MetricsEntity)
  implicit val metricsListFormat = jsonFormat1(MetricsList)

  implicit val requestCreateOrder = jsonFormat3(RequestCreateOrder)

  implicit val requestCreateUserFormat = jsonFormat4(RequestCreateUser)
  implicit val responseCreateUserFormat = jsonFormat1(ServerResponse)

  implicit val orderFormat = jsonFormat4(OrderEntity)


}