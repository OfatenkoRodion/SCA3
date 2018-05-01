package util

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import analysis.javaMetrics.QuantitativeMetricsJava
import com.typesafe.config.Config
import com.typesafe.scalalogging.StrictLogging
import dbGroup.DBModuleImpl
import module.Configuration

trait Mattcher  extends DBModuleImpl  with StrictLogging with Configuration{
  this: Configuration =>

  implicit val system: ActorSystem
  implicit val materializerZoom = ActorMaterializer()
  implicit val ecZoom = system.dispatcher

  def matchCodes(startCode: Int, filePathName: String, idMetricOrder: Option[Long]) ={

    idMetricOrder match {
      case Some(id) =>

        startCode match {
          case 101 =>
            val temp = QuantitativeMetricsJava.getBlankStrCount(filePathName)
             db.run(order_metricsDal.findById(id)).map {
               case Some(entyty)=> db.run(order_metricsDal.update(entyty.copy(result = Option{temp})))
               case None => None
             }
            println(temp)
          case 102 =>
            val temp = QuantitativeMetricsJava.getCommentsCount(filePathName)
            db.run(order_metricsDal.findById(id)).map {
              case Some(entyty)=> db.run(order_metricsDal.update(entyty.copy(result = Option{temp})))
              case None => None
            }
            println(temp)
          case 103 =>
            val temp = QuantitativeMetricsJava.getRatioOfComments(filePathName)
            db.run(order_metricsDal.findById(id)).map {
              case Some(entyty)=> db.run(order_metricsDal.update(entyty.copy(result = Option{temp})))
              case None => None
            }
            println(temp)
          case 104 => println(startCode)

          case _ => println("No handler for "+startCode+" code")
        }

      case None => println("Results cant be saved for start code "+ startCode)
    }


  }

}
