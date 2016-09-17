/*                                                                      *\
**                                                                      **
**      __  __ _________ _____          ©Mort BI                        **
**     |  \/  / () | () |_   _|         (c) 2015                        **
**     |_|\/|_\____|_|\_\ |_|           http://www.bigeyedata.com       **
**                                                                      **
\*                                                                      */
package com.bigeyedata.reactiveprogramming.aggregator

import akka.actor.{Actor, ActorRef, Props}
import akka.contrib.pattern.Aggregator
import akka.util.Timeout
import com.bigeyedata.reactiveprogramming.aggregator.PageContentFetcher.FetchPageContent
import com.bigeyedata.reactiveprogramming.aggregator.WordCounterAggregator.{AnalysisResult, BadCommand, StartAggregation}
import com.bigeyedata.reactiveprogramming.aggregator.WordCounterServer.AggregatedAnalysisResult

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object WordCounterAggregator {
  def props: Props = Props(new WordCounterAggregator)

  case class StartAggregation(target: ActorRef, urls: Seq[String])
  case object BadCommand
  case class AnalysisResult(wordToCount: Seq[(String, Long)])
}

class WordCounterAggregator extends Actor with Aggregator {
  expectOnce {
    case StartAggregation(target, urls) =>
      new Handler(target, urls, sender)
    case _ =>
      sender ! BadCommand
      context stop self
  }

  class Handler(target: ActorRef, urls: Seq[String], originalSender: ActorRef) {
    var analysisResults = Set.empty[AnalysisResult]

    context.system.scheduler.scheduleOnce(10.seconds, self, Timeout)
    expect {
      case Timeout =>
        respondIfDone(respondAnyway = true)
    }

    urls.foreach { uri =>
      target ! FetchPageContent(uri)
      expectOnce {
        case result: AnalysisResult =>
          analysisResults += result
          respondIfDone()
      }
    }

    def respondIfDone(respondAnyway: Boolean = false) = {
      import MapSeqImplicits._

      if (respondAnyway || analysisResults.size == urls.size) {
        val wordToCounts = analysisResults.flatMap(_.wordToCount).reduceByKey(_ + _)
        originalSender ! AggregatedAnalysisResult(wordToCounts)
        context stop self
      }
    }
  }

}
