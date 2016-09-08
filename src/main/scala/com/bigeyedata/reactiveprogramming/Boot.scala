/*                                                                      *\
**                                                                      **
**      __  __ _________ _____          ©Mort BI                        **
**     |  \/  / () | () |_   _|         (c) 2015                        **
**     |_|\/|_\____|_|\_\ |_|           http://www.bigeyedata.com       **
**                                                                      **
\*                                                                      */
package com.bigeyedata.reactiveprogramming

import akka.actor.{Props, ActorSystem}
import com.bigeyedata.reactiveprogramming.Messages.StartAnalysisWebPages

object Boot extends App {
  val system = ActorSystem("wordCounter")
  val receiver = system.actorOf(Props(new WordCounterReceiver))
  val client = system.actorOf(Props(new WordCounterClient))

  client ! StartAnalysisWebPages(Seq("http://www.baidu.com", "http://www.jianshu.com"), receiver)

  Thread.sleep(10000)
  system.shutdown()
}
