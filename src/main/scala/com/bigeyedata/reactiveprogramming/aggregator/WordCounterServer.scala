/*                                                                      *\
**                                                                      **
**      __  __ _________ _____          ©Mort BI                        **
**     |  \/  / () | () |_   _|         (c) 2015                        **
**     |_|\/|_\____|_|\_\ |_|           http://www.bigeyedata.com       **
**                                                                      **
\*                                                                      */
package com.bigeyedata.reactiveprogramming.aggregator

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.routing.RoundRobinPool
import com.bigeyedata.reactiveprogramming.aggregator.WordCounterAggregator.StartAggregation
import com.bigeyedata.reactiveprogramming.aggregator.WordCounterClient.AnalysisResultsFetched
import com.bigeyedata.reactiveprogramming.aggregator.WordCounterServer.{AggregatedAnalysisResult, FetchWebPages}

object WordCounterServer {
  def props: Props = Props(new WordCounterServer)

  case class FetchWebPages(uris: Seq[String], sender: ActorRef)
  case class AggregatedAnalysisResult(wordToCount: Seq[(String, Long)])
}

class WordCounterServer extends Actor with ActorLogging {
  val aggregator: ActorRef = context.actorOf(WordCounterAggregator.props, "aggregator")
  val analyst: ActorRef = context.actorOf(Props(new ContentWordCounter(aggregator)), "PageContentAnalyst")
  val fetchers = context.actorOf(RoundRobinPool(4).props(Props(new PageContentFetcher(analyst))), "fetchers")
  var client: ActorRef = _

  def receive: Receive = {
    case FetchWebPages(urls, clientActor) =>
      client = clientActor
      aggregator ! StartAggregation(fetchers, urls)
    case AggregatedAnalysisResult(wordToCount) =>
      log.info(s"the total count is ${wordToCount}")
      client ! AnalysisResultsFetched(wordToCount, wordToCount.map(_._2).sum)
  }
}
