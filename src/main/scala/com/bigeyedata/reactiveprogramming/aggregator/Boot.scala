/*                                                                      *\
**                                                                      **
**      __  __ _________ _____          ©Mort BI                        **
**     |  \/  / () | () |_   _|         (c) 2015                        **
**     |_|\/|_\____|_|\_\ |_|           http://www.bigeyedata.com       **
**                                                                      **
\*                                                                      */
package com.bigeyedata.reactiveprogramming.aggregator

import akka.actor.{ActorSystem, Props}
import com.bigeyedata.reactiveprogramming.aggregator.WordCounterClient.StartAnalysisWebPages

object Boot extends App {
  val system = ActorSystem("wordCounter")
  val server = system.actorOf(Props(new WordCounterServer), "server")
  val client = system.actorOf(WordCounterClient.props, "client")

  client ! StartAnalysisWebPages(Seq(
    "http://www.scala-lang.org/",
    "http://doc.akka.io/docs/akka/snapshot/contrib/aggregator.html",
    "http://doc.akka.io/docs/akka/2.4/scala/routing.html"), server)

  Thread.sleep(20000)
  system.shutdown()
}
