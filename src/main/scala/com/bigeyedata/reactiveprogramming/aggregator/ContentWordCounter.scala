/*                                                                      *\
**                                                                      **
**      __  __ _________ _____          ©Mort BI                        **
**     |  \/  / () | () |_   _|         (c) 2015                        **
**     |_|\/|_\____|_|\_\ |_|           http://www.bigeyedata.com       **
**                                                                      **
\*                                                                      */
package com.bigeyedata.reactiveprogramming.aggregator

import akka.actor.{Actor, ActorLogging, ActorRef}
import com.bigeyedata.reactiveprogramming.aggregator.ContentWordCounter.CountPageContent
import com.bigeyedata.reactiveprogramming.aggregator.WordCounterAggregator.AnalysisResult

object ContentWordCounter {

  case class CountPageContent(content: List[String])

}

class ContentWordCounter(aggregator: ActorRef) extends Actor with ActorLogging {
  import MapSeqImplicits._

  def receive: Receive = {
    case CountPageContent(content) =>
      val wordToCounts = content
        .flatMap(l => l.split(" "))
        .map(w => (w, 1L))
        .reduceByKey(_ + _)
      log.info(s"the wordToCounts of page is $wordToCounts")
      aggregator ! AnalysisResult(wordToCounts)
  }
}
