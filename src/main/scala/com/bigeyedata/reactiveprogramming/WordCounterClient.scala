/*                                                                      *\
**                                                                      **
**      __  __ _________ _____          ©Mort BI                        **
**     |  \/  / () | () |_   _|         (c) 2015                        **
**     |_|\/|_\____|_|\_\ |_|           http://www.bigeyedata.com       **
**                                                                      **
\*                                                                      */
package com.bigeyedata.reactiveprogramming

import akka.actor.{Actor, ActorLogging}
import com.bigeyedata.reactiveprogramming.Messages.{AnalysisResultsFetched, FetchWebPages, StartAnalysisWebPages}

class WordCounterClient extends Actor with ActorLogging {
  def receive: Receive = {
    case StartAnalysisWebPages(urls, receiver) =>
      receiver ! FetchWebPages(urls, self)
    case AnalysisResultsFetched(totalCount) =>
      log.info(s"the total word counts is $totalCount")
  }
}
