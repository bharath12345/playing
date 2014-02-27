package twitter

import akka.actor.Actor
import models.{Query, Tweet}
import play.api._

/**
 * Created by bharadwaj on 28/01/14.
 */
class TweetProcessor(query: String) extends Actor {

  Cache.init()

  def receive: Receive = {
    case tweet: Tweet => {
      Logger.info("tweet = " + tweet)
      //val htmlTweet = "<p>" + tweet.text + "</p>"

      val lctweet = tweet.text.toLowerCase()
      for(j <- Query.getStubs.indices) {
        val stub = Query.getStubs(j)
        if(lctweet.contains(stub)) {
          Cache.add(stub, tweet.text)
        }
      }
    }
  }
}
