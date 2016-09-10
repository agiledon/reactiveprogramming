/*                                                                      *\
**                                                                      **
**      __  __ _________ _____          ©Mort BI                        **
**     |  \/  / () | () |_   _|         (c) 2015                        **
**     |_|\/|_\____|_|\_\ |_|           http://www.bigeyedata.com       **
**                                                                      **
\*                                                                      */
package com.bigeyedata.reactiveprogramming.aggregator

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.bigeyedata.reactiveprogramming.aggregator.WordCounterClient.{AnalysisResultsFetched, StartAnalysisWebPages}
import com.bigeyedata.reactiveprogramming.aggregator.WordCounterServer.FetchWebPages

object WordCounterClient {
  def props = Props(new WordCounterClient)
  case class StartAnalysisWebPages(uris: Seq[String], requestReceiver: ActorRef)
  case class AnalysisResultsFetched(result: Long)
}

class WordCounterClient extends Actor with ActorLogging {
  def receive: Receive = {
    case StartAnalysisWebPages(urls, server) =>
      server ! FetchWebPages(urls, self)
    case AnalysisResultsFetched(totalCount) =>
      log.info(s"the total word counts is $totalCount")
  }
}
