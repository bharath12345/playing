package twitter

import akka.actor.Actor
import models.Tweet
import play.api._

/**
 * Created by bharadwaj on 28/01/14.
 */
class TweetProcessor extends Actor {
  def receive: Receive = {
    case tweet: Tweet => {
      Logger.info("tweet = " + tweet)
      Cache.tweets = tweet :: Cache.tweets

      val htmltweet = "<p>" + tweet.text + "</p>"
      Cache.tstream.write(htmltweet.getBytes())
    }
  }
}
