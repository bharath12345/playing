package twitter

import models.Tweet
import java.io.ByteArrayOutputStream

/**
 * Created by bharadwaj on 29/01/14.
 */
object Cache {
  var tweets = List[Tweet]()
  var tstream = new ByteArrayOutputStream
  var tweetCounter = 0

  def flush = {
    tweetCounter = 0
    tstream.reset()
    tweets = List[Tweet]()
  }
}
