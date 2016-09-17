/*                                                                      *\
**                                                                      **
**      __  __ _________ _____          ©Mort BI                        **
**     |  \/  / () | () |_   _|         (c) 2015                        **
**     |_|\/|_\____|_|\_\ |_|           http://www.bigeyedata.com       **
**                                                                      **
\*                                                                      */
package com.bigeyedata.reactiveprogramming.aggregator

import scala.collection.Iterable

object MapSeqImplicits {
  implicit class MapSeqWrapper(wordToCount: Iterable[(String, Long)]) {
    def reduceByKey(f: (Long, Long) => Long): Seq[(String, Long)] = {
      wordToCount.groupBy(_._1).map {
        case (word, counts) => (word, counts.map(_._2).foldLeft(0L)(f))
      }.toSeq
    }
  }
}
