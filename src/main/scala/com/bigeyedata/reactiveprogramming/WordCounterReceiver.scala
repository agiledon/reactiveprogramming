/*                                                                      *\
**                                                                      **
**      __  __ _________ _____          ©Mort BI                        **
**     |  \/  / () | () |_   _|         (c) 2015                        **
**     |_|\/|_\____|_|\_\ |_|           http://www.bigeyedata.com       **
**                                                                      **
\*                                                                      */
package com.bigeyedata.reactiveprogramming

import akka.actor.{ActorLogging, Actor, ActorRef, Props}
import akka.routing.RoundRobinPool
import com.bigeyedata.reactiveprogramming.WordCounterAggregator.StartAggregation
import com.bigeyedata.reactiveprogramming.WordCounterClient.AnalysisResultsFetched
import com.bigeyedata.reactiveprogramming.WordCounterReceiver.{AnalysisAggregatedResult, FetchWebPages}

object WordCounterReceiver {
  def props: Props = Props(new WordCounterReceiver)

  case class FetchWebPages(uris: Seq[String], sender: ActorRef)
  case class AnalysisAggregatedResult(count: Long)
}

class WordCounterReceiver extends Actor with ActorLogging {
  val aggregator: ActorRef = context.actorOf(WordCounterAggregator.props, "aggregator")
  val analyst: ActorRef = context.actorOf(Props(new PageContentAnalyst(aggregator)), "PageContentAnalyst")
  val fetchers = context.actorOf(RoundRobinPool(4).props(Props(new PageContentFetcher(analyst))), "fetchers")
  var totalCount: Long = 0
  var client: ActorRef = _

  def receive: Receive = {
    case FetchWebPages(uris, clientActor) =>
      client = clientActor
      aggregator ! StartAggregation(fetchers, uris)
    case AnalysisAggregatedResult(totalCount) =>
      log.info(s"the total count is ${totalCount}")
      client ! AnalysisResultsFetched(totalCount)
  }
}
