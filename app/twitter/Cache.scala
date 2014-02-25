package twitter

import models.Tweet
import java.io.ByteArrayOutputStream

/**
 * Created by bharadwaj on 29/01/14.
 */
object Cache {

  var tweetStream: Map[String, ByteArrayOutputStream] = Map()
  var tweetCounter: Map[String, Long] = Map()

  /**
   *
   * @param query
   * @param tweet
   */
  def add(query: String, tweet: String) = {
    getByteStream(query) match {
      case Some(byteStream) => {
        byteStream.write(tweet.getBytes())
      }
      case None => {
        val newStream = new ByteArrayOutputStream()
        tweetStream += (query -> newStream)
      }
    }

    getTweetCount(query) match {
      case Some(counter) => {
        tweetCounter += (query -> (counter + 1))
      }
      case None => {
        tweetCounter += (query -> 0)
      }
    }
  }

  /**
   *
   * @param query
   */
  def flush(query: String) = {
    getByteStream(query) match {
      case Some(byteStream) => byteStream.flush()
      case None =>
    }

    getTweetCount(query) match {
      case Some(count) => tweetCounter += (query -> 0)
      case None =>
    }
  }

  /**
   *
   * @param query
   * @return
   */
  def getByteStream(query: String): Option[ByteArrayOutputStream] = tweetStream.get(query)

  /**
   *
   * @param query
   * @return
   */
  def getTweetCount(query: String): Option[Long] = tweetCounter.get(query)
}
