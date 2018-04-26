package util

import analysis.javaMetrics.QuantitativeMetricsJava

object Mattcher {

  def matchCodes(startCode: Int, filePathName: String) ={

    startCode match {
      case 101 => println( QuantitativeMetricsJava.getBlankStrCount(filePathName))
      case 102 => println(QuantitativeMetricsJava.getCommentsCount(filePathName))
      case 103 => println(QuantitativeMetricsJava.getRatioOfComments(filePathName))
      case 104 => println(startCode)

      case _ => println("No handler for "+startCode+" code")
    }
  }

}
