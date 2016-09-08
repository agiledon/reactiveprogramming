/*                                                                      *\
**                                                                      **
**      __  __ _________ _____          ©Mort BI                        **
**     |  \/  / () | () |_   _|         (c) 2015                        **
**     |_|\/|_\____|_|\_\ |_|           http://www.bigeyedata.com       **
**                                                                      **
\*                                                                      */
package com.bigeyedata.reactiveprogramming

import akka.actor.{ActorRef, Actor}
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import akka.contrib.pattern.Aggregator
import com.bigeyedata.reactiveprogramming.Messages._

class WordCounterAggregator extends Actor with Aggregator {
  expectOnce {
    case StartAggregation(target, uris) =>
      new Handler(target, uris, sender)
    case _ =>
      sender ! BadCommand
      context stop self
  }

  class Handler(target: ActorRef, uris: Seq[String], originalSender: ActorRef) {
    var analysisResults = Set.empty[AnalysisResult]

    context.system.scheduler.scheduleOnce(10.seconds, self, Timeout)
    expect {
      case Timeout =>
        respondIfDone(respondAnyway = true)
    }

    uris.foreach { uri =>
      target ! FetchPageContent(uri)
      expectOnce {
        case result: AnalysisResult =>
          analysisResults += result
          respondIfDone()
      }
    }

    def respondIfDone(respondAnyway: Boolean = false) = {
      if (respondAnyway || analysisResults.size == uris.size) {
        originalSender ! AnalysisAggregatedResult(analysisResults.map(_.count).sum)
        context stop self
      }
    }
  }

}
