/*                                                                      *\
**                                                                      **
**      __  __ _________ _____          ©Mort BI                        **
**     |  \/  / () | () |_   _|         (c) 2015                        **
**     |_|\/|_\____|_|\_\ |_|           http://www.bigeyedata.com       **
**                                                                      **
\*                                                                      */
package com.bigeyedata.reactiveprogramming

import akka.actor.{Props, ActorSystem}
import com.bigeyedata.reactiveprogramming.WordCounterClient._

object Boot extends App {
  val system = ActorSystem("wordCounter")
  val receiver = system.actorOf(Props(new WordCounterReceiver))
  val client = system.actorOf(WordCounterClient.props)

  client ! StartAnalysisWebPages(Seq(
    "http://www.scala-lang.org/",
    "http://doc.akka.io/docs/akka/snapshot/contrib/aggregator.html",
    "http://doc.akka.io/docs/akka/2.4/scala/routing.html"), receiver)

  Thread.sleep(10000)
  system.shutdown()
}
