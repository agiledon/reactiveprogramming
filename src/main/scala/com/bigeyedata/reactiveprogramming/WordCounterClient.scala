/*                                                                      *\
**                                                                      **
**      __  __ _________ _____          ©Mort BI                        **
**     |  \/  / () | () |_   _|         (c) 2015                        **
**     |_|\/|_\____|_|\_\ |_|           http://www.bigeyedata.com       **
**                                                                      **
\*                                                                      */
package com.bigeyedata.reactiveprogramming

import akka.actor.{ActorRef, Props, Actor, ActorLogging}
import com.bigeyedata.reactiveprogramming.WordCounterClient._
import com.bigeyedata.reactiveprogramming.WordCounterReceiver.FetchWebPages

object WordCounterClient {
  def props = Props(new WordCounterClient)
  case class StartAnalysisWebPages(uris: Seq[String], requestReceiver: ActorRef)
  case class AnalysisResultsFetched(result: Long)
}

class WordCounterClient extends Actor with ActorLogging {
  def receive: Receive = {
    case StartAnalysisWebPages(urls, receiver) =>
      receiver ! FetchWebPages(urls, self)
    case AnalysisResultsFetched(totalCount) =>
      log.info(s"the total word counts is $totalCount")
  }
}
