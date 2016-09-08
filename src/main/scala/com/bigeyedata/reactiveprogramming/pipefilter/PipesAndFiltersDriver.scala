/*                                                                      *\
**                                                                      **
**      __  __ _________ _____          ©Mort BI                        **
**     |  \/  / () | () |_   _|         (c) 2015                        **
**     |_|\/|_\____|_|\_\ |_|           http://www.bigeyedata.com       **
**                                                                      **
\*                                                                      */
package com.bigeyedata.reactiveprogramming.pipefilter

import akka.actor.{Props, ActorSystem}

object PipesAndFiltersDriver extends App {
  val system = ActorSystem("pipeFilter")

  val orderText = "(encryption)(certificate)<order id='123'>product</order>"
  val rawOrderBytes = orderText.toCharArray.map(_.toByte)

  val orderManager = system.actorOf(Props[OrderManagementSystem], "OrderManagementSystem")
  val deduplicator = system.actorOf(Props(new Deduplicator(orderManager)), "Deduplicator")
  val authenticator = system.actorOf(Props(new Authenticator(deduplicator)), "Authenticator")
  val decrypter = system.actorOf(Props(new Decrypter(authenticator)), "Decrypter")
  val acceptance = system.actorOf(Props(new OrderAcceptanceEndpoint(decrypter)), "OrderAcceptanceEndpoint")

  acceptance ! rawOrderBytes
  acceptance ! rawOrderBytes

  Thread.sleep(5000)
  println("Pipe and Filter: is completed.")
}
