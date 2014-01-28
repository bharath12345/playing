package controllers

import akka.actor.Actor
import models.Tweet
import play.Logger

/**
 * Created by bharadwaj on 28/01/14.
 */
class TweetProcessor extends Actor {
  def receive: Receive = {
    case tweet: Tweet => {
      println("tweet = " + tweet)
      Logger.debug("tweet = " + tweet)
    }
  }
}
