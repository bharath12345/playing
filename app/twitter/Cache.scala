package twitter

import models.Tweet
import models.Query
import java.io.ByteArrayOutputStream

/**
 * Created by bharadwaj on 29/01/14.
 */
object Cache {

  sealed trait Period
  case class Period3()     extends Period
  case class Period30()    extends Period
  case class Period300()   extends Period
  case class Period1800()  extends Period
  case class Period10800() extends Period

  // map of counters for each period, having a map for each query-stub and its counter
  var tweetCounter: Map[Int, Map[String, Long]] = Map()
  for(i <- 0 until 5) {
    tweetCounter += (i -> Map())
  }

  def add(stub: String, tweet: String) = {
    for(i <- 0 until 5) {

      var tCounter: Map[String, Long] = tweetCounter.get(i) match {
        case Some(tCounter) => tCounter
        case None => {
          var nCounter: Map[String, Long] = Map()
          for(stub <- Query.getStubs) {
            nCounter += (stub -> 0)
          }
          tweetCounter += (i -> nCounter)
          nCounter
        }
      }

      tCounter.get(stub) match {
        case Some(counter) => {
          tCounter += (stub -> (counter + 1))
        }
        case None =>  {
          tCounter += (stub -> (0))
        }
      }
    }
  }

  def flush(period: Int) = {
    var tCounter: Map[String, Long] = tweetCounter(period)
    tCounter foreach {
      case (stub, counter) => {
        tCounter += (stub -> (0))
      }
    }
  }

  def getTweetCount(period: Int): Map[String, Long] = tweetCounter.get(period).get

}
