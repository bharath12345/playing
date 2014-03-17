package twitter

/**
 * Created by bharadwaj on 29/01/14.
 */
object TwitterCredentials {
  val at = System.getenv("TWITTER_APP_TOKEN")
  val as = System.getenv("TWITTER_APP_SECRET")

  val ck = System.getenv("TWITTER_CONSUMER_KEY")
  val cs = System.getenv("TWITTER_CONSUMER_SECRET")
}
