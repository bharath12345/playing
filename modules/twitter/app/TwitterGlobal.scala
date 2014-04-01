package twitter

import _root_.twitter._
import models.Refresh10800
import models.Refresh1800
import models.Refresh3
import models.Refresh30
import models.Refresh300
import models.twitter.{ThreeSecDAO, QueryStringDAO, Query, Statistics}
import play.api.{Logger}
import scala.slick.jdbc.JdbcBackend._
import scala.slick.jdbc.meta.MTable
import kafka.ConsumerGroupExample

/**
 * Created by bharadwaj on 25/03/14.
 */
object TwitterGlobal extends Configuration {

  def startTwitterStreaming = {
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

  def createTables = {

    // create tables if not exist
    db.withSession { implicit session: Session =>
      if (!MTable.getTables.list.exists(_.name.name == "QueryStrings"))
        QueryStringDAO.create
      if (!MTable.getTables.list.exists(_.name.name == "ThreeSec"))
        ThreeSecDAO.create
    }
  }

  def startKafkaConsumer = {
    val numThreads: Int = 4
    val topicName: String = "myTopic"
    // val groupId: String = "group0" <- for now this is hard-coded in Configuration
    val example: ConsumerGroupExample = new ConsumerGroupExample("localhost:2181", "group0", topicName)
    example.run(numThreads)
  }

  def onStart = {
    Logger.info("Twitter module has started")
    createTables
    startTwitterStreaming
  }

  def onStop = {
    Logger.info("Twitter module shutdown...")
  }
}
