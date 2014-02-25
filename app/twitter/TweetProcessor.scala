package twitter

import akka.actor.Actor
import models.Tweet
import play.api._

/**
 * Created by bharadwaj on 28/01/14.
 */
class TweetProcessor(query: String) extends Actor {
  def receive: Receive = {
    case tweet: Tweet => {
      Logger.info("tweet = " + tweet)
      val htmlTweet = "<p>" + tweet.text + "</p>"
      Cache.add(query, htmlTweet)
    }
  }
}
