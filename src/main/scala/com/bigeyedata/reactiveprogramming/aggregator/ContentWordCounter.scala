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
  def receive: Receive = {
    case CountPageContent(content) =>
      val wordToCounts = content
        .flatMap(l => l.split(" "))
        .map(w => (w, 1))
        .groupBy(_._1)
        .map {
        case (word, counts) => (word, counts.foldLeft(0L)(_ + _._2))
      }.toSeq
      log.info(s"the wordToCounts of page is $wordToCounts")
      aggregator ! AnalysisResult(wordToCounts)
  }
}
