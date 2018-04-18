package util

import dbGroup.entities.LanguageEntity
import de.knutwalker.akka.http.support.CirceHttpSupport
import de.knutwalker.akka.stream.support.CirceStreamSupport
import spray.json.DefaultJsonProtocol

trait JsonSupport extends DefaultJsonProtocol with CirceHttpSupport with CirceStreamSupport {

  implicit val languageEntityFormat = jsonFormat2(LanguageEntity)
}