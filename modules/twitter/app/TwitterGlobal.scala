import _root_.twitter._
import models._
import models.Refresh10800
import models.Refresh1800
import models.Refresh3
import models.Refresh30
import models.Refresh300
import models.twitter.{Query, Statistics}
import play.api.{Logger}

/**
 * Created by bharadwaj on 25/03/14.
 */
object TwitterGlobal {

  def onStart() = {
    Logger.info("Twitter module has started")

    Query.addToQuery("india")
    Query.addToQuery("modi")
    Query.addToQuery("rahul")
    Query.addToQuery("kejri")
    val query: String = Query.getQuery

    val streamer = StartStreamer.startStreamerProcessor(query)
    streamer ! query


    Statistics.attach(Refresh3())
    Statistics.attach(Refresh30())
    Statistics.attach(Refresh300())
    Statistics.attach(Refresh1800())
    Statistics.attach(Refresh10800())
  }

  def onStop() {
    Logger.info("Twitter module shutdown...")
  }
}
