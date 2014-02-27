package twitter

import models.Tweet
import models.Query
import java.io.ByteArrayOutputStream
import play.api.Logger

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



  def init() = {
    for(i <- 0 until 5) {
      var tCounter: Map[String, Long] = Map()
      for(j <- Query.getStubs.indices) {
        val stub = Query.getStubs(j)
        Logger.info(s"adding stub to cache = $stub for period = $i")
        tCounter += (stub -> (0))
      }
      tweetCounter += (i -> tCounter)
    }
  }

  def add(stub: String, tweet: String) = {
    for(i <- 0 until 5) {
      var tCounter: Map[String, Long] = tweetCounter.get(i) match {
        case Some(tCounter) => tCounter
        case None => {
          Logger.error(s"ERROR!! period not initialized for period = $i")
          throw new Exception(s"ERROR!! period not initialized for period = $i")
        }
      }

      tCounter.get(stub) match {
        case Some(counter) => {
          tCounter += (stub -> (counter + 1))
          tweetCounter += (i -> tCounter)
        }
        case None =>  {
          Logger.error(s"ERROR!! stub not initialized for stub = $stub and period = $i")
          throw new Exception(s"ERROR!! stub not initialized for stub = $stub and period = $i")
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
    tweetCounter += (period -> tCounter)
  }

  def getTweetCount(period: Int): Map[String, Long] = tweetCounter.get(period).get

}
