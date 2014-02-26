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
      //val htmlTweet = "<p>" + tweet.text + "</p>"

      val indiaPattern = "\\b(?i)india.*"
      val modiPattern  = "\\b(?i)modi.*"
      val rahulPattern = "\\b(?i)rahul.*"
      val kejriPattern = "\\b(?i)kejri.*"

      if(tweet.text.matches(indiaPattern)) {
        Cache.add("india", tweet.text)
      }

      if(tweet.text.matches(modiPattern)) {
        Cache.add("modi", tweet.text)
      }

      if(tweet.text.matches(rahulPattern)) {
        Cache.add("rahul", tweet.text)
      }

      if(tweet.text.matches(kejriPattern)) {
        Cache.add("kejri", tweet.text)
      }
    }
  }
}
