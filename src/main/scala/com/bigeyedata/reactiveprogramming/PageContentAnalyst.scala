/*                                                                      *\
**                                                                      **
**      __  __ _________ _____          ©Mort BI                        **
**     |  \/  / () | () |_   _|         (c) 2015                        **
**     |_|\/|_\____|_|\_\ |_|           http://www.bigeyedata.com       **
**                                                                      **
\*                                                                      */
package com.bigeyedata.reactiveprogramming

import akka.actor.{Actor, ActorLogging, ActorRef}
import com.bigeyedata.reactiveprogramming.PageContentAnalyst.AnalysisWebPage
import com.bigeyedata.reactiveprogramming.WordCounterAggregator.AnalysisResult

object PageContentAnalyst {
  case class AnalysisWebPage(content: List[String])
}

class PageContentAnalyst(mediator: ActorRef) extends Actor with ActorLogging {
  def receive: Receive = {
    case AnalysisWebPage(content) =>
      val count = content.flatMap(l => l.split(" ")).distinct.size
      log.info(s"the count of page is $count")
      mediator ! AnalysisResult(count)
  }
}
