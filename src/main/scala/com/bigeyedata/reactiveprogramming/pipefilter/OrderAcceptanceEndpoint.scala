/*                                                                      *\
**                                                                      **
**      __  __ _________ _____          ©Mort BI                        **
**     |  \/  / () | () |_   _|         (c) 2015                        **
**     |_|\/|_\____|_|\_\ |_|           http://www.bigeyedata.com       **
**                                                                      **
\*                                                                      */
package com.bigeyedata.reactiveprogramming.pipefilter

import akka.actor.{ActorLogging, ActorRef, Actor}

class OrderAcceptanceEndpoint(nextFilter: ActorRef) extends Actor with ActorLogging {
  def receive: Receive = {
    case rawOrder: Array[Byte] =>
      val text = new String(rawOrder)
      log.info(s"OrderAcceptanceEndpoint: processing $text")
      nextFilter ! ProcessIncomingOrder(rawOrder)
  }
}

class Decrypter(nextFilter: ActorRef) extends Actor with ActorLogging {
  def receive: Receive = {
    case message: ProcessIncomingOrder =>
      val text = new String(message.orderInfo)
      log.info(s"Decrypter: processing $text")
      val orderText = text.replace("(encryption)", "")
      nextFilter !  ProcessIncomingOrder(orderText.toCharArray.map(_.toByte))
  }
}

class Authenticator(nextFilter: ActorRef) extends Actor with ActorLogging {
  def receive: Receive = {
    case message: ProcessIncomingOrder =>
      val text = new String(message.orderInfo)
      log.info(s"Authenticator: processing $text")
      val orderText = text.replace("(certificate)", "")
      nextFilter !  ProcessIncomingOrder(orderText.toCharArray.map(_.toByte))
  }
}

class Deduplicator(nextFilter: ActorRef) extends Actor with ActorLogging {
  val processedOrderIds = scala.collection.mutable.Set[String]()

  def orderIdFrom(orderText: String): String = {
    val orderIdIndex = orderText.indexOf("id='") + 4
    val orderIdLastIndex = orderText.indexOf("'", orderIdIndex)
    orderText.substring(orderIdIndex, orderIdLastIndex)
  }

  def receive: Receive = {
    case message: ProcessIncomingOrder =>
      val text = new String(message.orderInfo)
      log.info(s"Deduplicator: processing $text")
      val orderId = orderIdFrom(text)
      if (processedOrderIds.add(orderId)) {
        nextFilter ! message
      } else {
        log.info(s"Deduplicator: found duplicate order with $orderId")
      }
  }
}

class OrderManagementSystem extends Actor with ActorLogging {
  def receive: Receive = {
    case message: ProcessIncomingOrder =>
      val text = new String(message.orderInfo)
      log.info(s"OrderManagementSystem: Processing unique order: $text")
  }
}
