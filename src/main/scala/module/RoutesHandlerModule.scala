package module

import java.util.Date

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.StrictLogging
import dbGroup.DBModuleImpl
import dbGroup.entities._
import rest.entities.{RequestCreateOrder, RequestCreateUser}
import util.{JsonSupport, UserCreationException}

import scala.concurrent.Future

trait RoutesHandlerModule {

  implicit val system: ActorSystem

  def addLanguage(language:String): Future[LanguageEntity]
  def getLanguageList: Future[Seq[LanguageEntity]]
  def getMetricsList(language: String): Future[Seq[MetricsEntity]]
  def createUser(request: RequestCreateUser): Future[String]
  def createOrder(requestCreateOrder: RequestCreateOrder): Future[String]
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

  def getLanguageList: Future[Seq[LanguageEntity]]={
    db.run(languageDal.getLanguageList)
  }

  def getMetricsList(language: String): Future[Seq[MetricsEntity]]={
    db.run(languageDal.getLanguage(language)).flatMap {
      case Some(language) => language.id match {
        case Some(id)=>db.run(metricsDal.getMetricsList(id))
        case None =>Future(Seq())
      }
      case None => Future(Seq())
    }
  }

  def createUser(request: RequestCreateUser): Future[String] ={

    for {

    ifPasswordEquals <- Future{ if (request.password1.equals(request.password2)) false else true }

    ifLoginExist <-db.run(userDal.getUserByLogin(request.login)).map {
      case Some(user) => true
      case None => false}

     ifEmailExist <- db.run(userDal.getUserByEmail(request.email)).map {
       case Some(user) => true
       case None => false}

     result <- Future [String] {
       if(ifLoginExist){throw UserCreationException("Exception: login is exist")} else
       if(ifEmailExist){throw UserCreationException("Exception: email is exist")} else
       if(ifPasswordEquals){throw UserCreationException("Exception: password not equals")} else {
         db.run(userDal.save(UserEntity(None, request.login, request.password1, request.email, None, new  Date().toString)))
        "User was created"
       }
     }
    } yield result
  }

  def createOrder(requestCreateOrder: RequestCreateOrder): Future[String]={
    for {
      order <- db.run(orderDal.save(OrderEntity(None, requestCreateOrder.login, new Date().toString)))
      result <- order.id match {
          case Some(id) =>  requestCreateOrder.metricsList.foreach { metric=>
              db.run(order_metricsDal.save(OrderMetricsEntity(None, id, metric, "Created", None)))
          }
            Future("Your order has been accepted")
          case None => Future("Error: can not add an order")
        }
    } yield result
  }

}