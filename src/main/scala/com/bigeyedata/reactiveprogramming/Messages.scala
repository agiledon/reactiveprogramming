/*                                                                      *\
**                                                                      **
**      __  __ _________ _____          ©Mort BI                        **
**     |  \/  / () | () |_   _|         (c) 2015                        **
**     |_|\/|_\____|_|\_\ |_|           http://www.bigeyedata.com       **
**                                                                      **
\*                                                                      */
package com.bigeyedata.reactiveprogramming

import akka.actor.ActorRef

object Messages {
  case class StartAnalysisWebPages(uris: Seq[String], requestReceiver: ActorRef)
  case class FetchWebPages(uris: Seq[String], sender: ActorRef)
  case class FetchPageContent(uri: String)

  case class StartAggregation(target: ActorRef, uris: Seq[String])
  case object BadCommand
  case class AnalysisWebPage(content: List[String])
  case class AnalysisResult(count: Long)
  case class AnalysisAggregatedResult(count: Long)
  case class AnalysisResultsFetched(result: Long)
}
