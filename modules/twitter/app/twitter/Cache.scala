package twitter

import models.twitter.Tweet
import models.twitter.Query
import java.io.ByteArrayOutputStream
import play.api.Logger
import models._

/**
 * Created by bharadwaj on 29/01/14.
 */
object Cache {

  // map of counters for each period, having a map for each query-stub and its counter
  val tweetCounter = scala.collection.mutable.Map[Refresh,Map[String, Long]]()

  def init() = {
    for(i <- 0 until 5) {
      var tCounter: Map[String, Long] = Map()
      for(j <- Query.getStubs.indices) {
        val stub = Query.getStubs(j)
        Logger.info(s"adding stub to cache = $stub for period = $i")
        tCounter += (stub -> (0))
      }
      tweetCounter += (Refresh(i) -> tCounter)
    }
  }

  def add(stub: String, tweet: String) = {
    for(i <- 0 until 5) {
      val refresh = Refresh(i)

      var tCounter: Map[String, Long] = tweetCounter.get(refresh) match {
        case Some(tCounter) => tCounter
        case None => {
          Logger.error(s"ERROR!! period not initialized for period = $i")
          throw new Exception(s"ERROR!! period not initialized for period = $i")
        }
      }

      tCounter.get(stub) match {
        case Some(counter) => {
          tCounter += (stub -> (counter + 1))
          tweetCounter += (refresh -> tCounter)
        }
        case None =>  {
          Logger.error(s"ERROR!! stub not initialized for stub = $stub and period = $i")
          throw new Exception(s"ERROR!! stub not initialized for stub = $stub and period = $i")
        }
      }
    }
  }

  def flush(refresh: Refresh) = {
    var tCounter: Map[String, Long] = tweetCounter(refresh)
    tCounter foreach {
      case (stub, counter) => {
        tCounter += (stub -> (0))
      }
    }
    tweetCounter += (refresh -> tCounter)
  }

  def getTweetCount(refresh: Refresh): Option[Map[String, Long]] = {
    tweetCounter.get(refresh)
  }

}
