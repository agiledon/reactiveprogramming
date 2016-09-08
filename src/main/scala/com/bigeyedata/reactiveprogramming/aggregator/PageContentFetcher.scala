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
import com.bigeyedata.reactiveprogramming.aggregator.PageContentFetcher.FetchPageContent

import scala.io.Source

object PageContentFetcher {
  case class FetchPageContent(uri: String)
}

class PageContentFetcher(analyst: ActorRef) extends Actor with ActorLogging {
  def receive: Receive = {
    case FetchPageContent(url) =>
      val content = Source.fromURL(url).getLines().toList
      log.info(s"fetch url content $url")
      analyst ! CountPageContent(content)
  }
}
