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

class ContentWordCounter(mediator: ActorRef) extends Actor with ActorLogging {
  def receive: Receive = {
    case CountPageContent(content) =>
      val count = content.flatMap(l => l.split(" ")).distinct.size
      log.info(s"the count of page is $count")
      mediator ! AnalysisResult(count)
  }
}
