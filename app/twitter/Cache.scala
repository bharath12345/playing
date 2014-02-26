package twitter

import models.Tweet
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

  var p3TweetCounter: Map[String, Long] = Map()
  var p30TweetCounter: Map[String, Long] = Map()
  var p300TweetCounter: Map[String, Long] = Map()
  var p1800TweetCounter: Map[String, Long] = Map()
  var p10800TweetCounter: Map[String, Long] = Map()


  /**
   *
   * @param phrase
   * @param tweet
   */
  def add(phrase: String, tweet: String) = {
    getP3TweetCount(phrase) match {
      case Some(counter) => {
        p3TweetCounter     += (phrase -> (p30TweetCounter(phrase)    + 1))
        p30TweetCounter    += (phrase -> (p30TweetCounter(phrase)    + 1))
        p300TweetCounter   += (phrase -> (p300TweetCounter(phrase)   + 1))
        p1800TweetCounter  += (phrase -> (p1800TweetCounter(phrase)  + 1))
        p10800TweetCounter += (phrase -> (p10800TweetCounter(phrase) + 1))
      }
      case None => {
        p3TweetCounter     += (phrase -> 0)
        p30TweetCounter    += (phrase -> 0)
        p300TweetCounter   += (phrase -> 0)
        p1800TweetCounter  += (phrase -> 0)
        p10800TweetCounter += (phrase -> 0)
      }
    }
  }

  /**
   *
   * @param phrase
   */
  def flush3(phrase: String) = {
    getP3TweetCount(phrase) match {
      case Some(count) => {
        p3TweetCounter += (phrase -> 0)
      }
    }
  }

  def flush30(phrase: String) = {
    getP30TweetCount(phrase) match {
      case Some(count) => {
        p3TweetCounter += (phrase -> 0)
      }
    }
  }

  def flush300(phrase: String) = {
    getP300TweetCount(phrase) match {
      case Some(count) => {
        p3TweetCounter += (phrase -> 0)
      }
    }
  }

  def flush1800(phrase: String) = {
    getP1800TweetCount(phrase) match {
      case Some(count) => {
        p3TweetCounter += (phrase -> 0)
      }
    }
  }

  def flush10800(phrase: String) = {
    getP10800TweetCount(phrase) match {
      case Some(count) => {
        p3TweetCounter += (phrase -> 0)
      }
    }
  }

  /**
   *
   * @param phrase
   * @return
   */
  def getP3TweetCount(phrase: String): Option[Long] = p3TweetCounter.get(phrase)

  def getP30TweetCount(phrase: String): Option[Long] = p3TweetCounter.get(phrase)

  def getP300TweetCount(phrase: String): Option[Long] = p3TweetCounter.get(phrase)

  def getP1800TweetCount(phrase: String): Option[Long] = p3TweetCounter.get(phrase)

  def getP10800TweetCount(phrase: String): Option[Long] = p3TweetCounter.get(phrase)
}
