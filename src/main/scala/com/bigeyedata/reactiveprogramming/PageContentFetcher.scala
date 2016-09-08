/*                                                                      *\
**                                                                      **
**      __  __ _________ _____          ©Mort BI                        **
**     |  \/  / () | () |_   _|         (c) 2015                        **
**     |_|\/|_\____|_|\_\ |_|           http://www.bigeyedata.com       **
**                                                                      **
\*                                                                      */
package com.bigeyedata.reactiveprogramming

import akka.actor.{Actor, ActorLogging, ActorRef}
import com.bigeyedata.reactiveprogramming.Messages.{AnalysisWebPage, FetchPageContent}

import scala.io.Source

class PageContentFetcher(analyst: ActorRef) extends Actor with ActorLogging {
  def receive: Receive = {
    case FetchPageContent(uri) =>
      val content = Source.fromURL(uri).getLines().toList
      log.info(s"fetch url content $uri")
      analyst ! AnalysisWebPage(content)
  }
}
