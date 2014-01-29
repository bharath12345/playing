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
      println("tweet = " + tweet)
      Logger.info("tweet = " + tweet)
    }
  }
}
