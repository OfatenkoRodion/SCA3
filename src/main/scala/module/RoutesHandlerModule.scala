package module

import akka.actor.ActorSystem
import akka.actor.Status.Failure
import akka.stream.ActorMaterializer
import util.JsonSupport
import com.typesafe.scalalogging.StrictLogging
import dbGroup.DBModuleImpl
import dbGroup.entities.LanguageEntity

import scala.concurrent.Future

trait RoutesHandlerModule {

  implicit val system: ActorSystem

  def addLanguage(language:String): Future[LanguageEntity]

}

trait RoutesHandlerModuleImpl extends RoutesHandlerModule with DBModuleImpl with StrictLogging with Configuration with JsonSupport {
  this: Configuration =>

  implicit val materializerZoom = ActorMaterializer()
  implicit val ecZoom = system.dispatcher

  def addLanguage(language:String): Future[LanguageEntity]={
   db.run(languageDal.getLanguage(language)).flatMap {
      case Some(t) => Future(t)
      case None => db.run(languageDal.save(LanguageEntity(None, language)))
    }
  }
}