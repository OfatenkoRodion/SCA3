package module

import java.util.Date

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import analysis.DownloadFile
import analysis.javaMetrics.QuantitativeMetricsJava
import com.typesafe.scalalogging.StrictLogging
import dbGroup.DBModuleImpl
import dbGroup.entities._
import rest.entities.{RequestCreateOrder, RequestCreateUser}
import util.{JsonSupport, JwtImpl, Mattcher, UserCreationException}
import scalaz.Scalaz._

import scala.concurrent.Future

trait RoutesHandlerModule {

  implicit val system: ActorSystem

  def addLanguage(language:String): Future[LanguageEntity]
  def getLanguageList: Future[Seq[LanguageEntity]]
  def getMetricsList(language: String): Future[Seq[MetricsEntity]]
  def createUser(request: RequestCreateUser): Future[String]
  def login(request: RequestCreateUser): Future[String]
  def createOrder(requestCreateOrder: RequestCreateOrder): Future[String]
  def startOrder(orderId: Long): Future[String]
}

trait RoutesHandlerModuleImpl extends RoutesHandlerModule with DBModuleImpl with StrictLogging with Configuration with JsonSupport with Mattcher{
  this: Configuration =>

  override implicit val materializerZoom = ActorMaterializer()
  override implicit val ecZoom = system.dispatcher

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

  def login(request: RequestCreateUser): Future[String] ={

    for {

      ifPasswordEquals <- Future{ if (request.password1.equals(request.password2)) false else true }

      ifLoginExist <-db.run(userDal.getUserByLogin(request.login)).map {
        case Some(user) => false
        case None => true}

      ifEmailExist <- db.run(userDal.getUserByEmail(request.email)).map {
        case Some(user) => false
        case None => true}

      result <-  {
        if(ifLoginExist){throw UserCreationException("Exception: login is not exist")} else
        if(ifEmailExist){throw UserCreationException("Exception: email is not exist")} else
        if(ifPasswordEquals){throw UserCreationException("Exception: password not equals")} else {
          db.run(userDal.save(UserEntity(None, request.login, request.password1, request.email, None, new  Date().toString)))
          db.run(userDal.getUserByEmail(request.email)).map {
            case Some(user) =>
              val token=JwtImpl.getToken(user.login,user.password)
              db.run(userDal.updateUserByEmail(request.email,token))
              token
            case None => "Something went wrong"
          }
        }
      }
    } yield result
  }

  def createOrder(requestCreateOrder: RequestCreateOrder): Future[String]={
    for {
      order <- db.run(orderDal.save(OrderEntity(None, requestCreateOrder.login, new Date().toString,requestCreateOrder.link)))

      result <- order.id match {
          case Some(id) =>  requestCreateOrder.metricsList.foreach { metric=>
              db.run(order_metricsDal.save(OrderMetricsEntity(None, id, metric, "Created", None)))
          }
            Future("Your order has been created")
          case None => Future("Error: can not add an order")
        }
    } yield result
  }




  def startOrder(orderId: Long): Future[String] ={

    for {
      file <- downloadFile(orderId)
      orderMetric <- db.run(order_metricsDal.findByOrderId(orderId))
      temp <- Future{orderMetric.foreach { u => db.run(metricsDal.getMetricsById(u.metricsId)).map {

        case Some(entity) =>matchCodes(entity.startCode,file, u.id)
        case None => println(" error with " + u.metricsId + " code")
          }
        }
      }

      res <- Future(QuantitativeMetricsJava.getBlankStrCount(file))
    } yield res

  }

  def downloadFile(orderId: Long): Future[String] ={

    val res: scalaz.OptionT[Future,String] =for {
      order <- scalaz.OptionT(db.run(orderDal.findByOrderId(orderId)))
      file <- scalaz.OptionT(Future.successful(Option(DownloadFile.start(order.link, ConfigurationModuleClass.folderUrl,orderId.toString))))
    } yield file

    res.run.map(_.get)
  }

}