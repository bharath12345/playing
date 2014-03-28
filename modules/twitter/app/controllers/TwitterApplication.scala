package controllers.twitter

import play.api.mvc._
import play.api._
import play.api.libs.json.{Writes, JsNull, Json, JsValue}

import models.twitter._
import models.Refresh

import twitter._

import scala.concurrent._
import ExecutionContext.Implicits.global
import notifiers.EmailNotifier
import securesocial.core.SecureSocial
import scala.slick.jdbc.JdbcBackend.{Database, Session}
import _root_.twitter.Configuration


/**
 * Created by bharadwaj on 27/01/14.
 */
object TwitterApplication extends Controller with SecureSocial with Configuration {

  case class Tweet(time: Long, stub: String, counter: Long)
  case class Tweets(tweets: Seq[Tweet])

  implicit val tweetWrites = new Writes[Tweet] {
    def writes(tweet: Tweet) = Json.obj(
      "time"    -> tweet.time,
      "stub"    -> tweet.stub,
      "counter" -> tweet.counter
    )
  }

  implicit val tweetsWrites = new Writes[Tweets] {
    def writes(tweets: Tweets) = Json.obj(
      "tweets" -> tweets.tweets)
  }

  def dashboard(query: String) = Action {
    implicit request =>
      Query.addToQuery(query)
      val streamer = StartStreamer.startStreamerProcessor(query)
      streamer ! query
      val wsURL: String = controllers.twitter.routes.TwitterApplication.live(0).webSocketURL()
      val historyUrl = controllers.twitter.routes.TwitterApplication.history(0).absoluteURL()
      Ok(views.html.dashboard(Query.getStubs)(wsURL)(historyUrl))
  }

  def elections(period: Int) = SecuredAction {
    implicit request =>

      Query.addToQuery("india")
      Query.addToQuery("modi")
      Query.addToQuery("rahul")
      Query.addToQuery("kejri")
      val query: String = Query.getQuery

      val streamer = StartStreamer.startStreamerProcessor(query)
      streamer ! query

      val wsUrl = controllers.twitter.routes.TwitterApplication.live(period).webSocketURL()

      val historyUrl = period match {
        case 0 => controllers.twitter.routes.TwitterDbDataController.last2Minutes.absoluteURL()
        case 1 => controllers.twitter.routes.TwitterDbDataController.last15Minutes.absoluteURL()
        case 2 => controllers.twitter.routes.TwitterDbDataController.last200Minutes.absoluteURL()
        case 3 => controllers.twitter.routes.TwitterDbDataController.last1200Minutes.absoluteURL()
        case 4 => controllers.twitter.routes.TwitterDbDataController.last7200Minutes.absoluteURL()
      }

      Ok(views.html.dashboard(Query.getStubs)(wsUrl)(historyUrl))
  }

  def live(period: Int) = WebSocket.async[JsValue] {
    request =>
      Statistics.attach(Refresh(period))
  }

  def sendmail = Action {
    EmailNotifier.sendMail
    Ok("sent something!")
  }

  def history(period: Int) = Action {
    val queryIdMap = scala.collection.mutable.Map[Long, String]()
    db.withSession { implicit session: Session =>
      val history: List[ThreeSec] = ThreeSecDAO.findAll
      Logger.info(s"number of history records fetched = " + history.length)

      var tweets = scala.collection.mutable.Seq[Tweet]()
      for(h <- history) {

        val queryString: String = queryIdMap.get(h.queryString) match {
          case Some(qs) => qs
          case None => {
            val qs: String = QueryStringDAO.findById(h.queryString).get.queryString
            queryIdMap += (h.queryString -> qs)
            qs
          }
        }

        val tweet = Tweet(h.dateTime.getMillis/1000, queryString, h.count)
        tweets = tweets :+ tweet
      }

      Logger.info(s"number of tweets in the sequence = " + tweets.length)
      val t: Tweets = Tweets(tweets)
      val json = Json.toJson(t)
      Ok(Json.stringify(json))
    }
  }

}
