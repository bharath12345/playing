package models

import akka.actor.Actor
import play.api.Logger
import _root_.twitter.Configuration
import scala.slick.jdbc.JdbcBackend.{Database, Session}
import play.api.libs.json._
import play.api.libs.functional.syntax._
import models.twitter._
import org.joda.time.DateTime
import play.api.libs.json.JsSuccess
import models.twitter.PersistenceMsg
import models.twitter.ThreeSec
import models.twitter.QueryString

/**
 * Created by bharadwaj on 25/03/14.
 */

case class TweetCounter(stub: String, tweets: Long)
case class TweetJson(timestamp: Long, period: Int, tc: Seq[TweetCounter])


class PersistorActor() extends Actor with Configuration {

  /*
    Json Msg: {"timestamp":1395763755674,"period":0,"values":[{"stub":"kejri","tweets":4},{"stub":"rahul","tweets":1},{"stub":"modi","tweets":6},{"stub":"india","tweets":6}]}
   */

  implicit val tcReads: Reads[TweetCounter] = (
    (JsPath \ "stub").read[String] and
      (JsPath \ "tweets").read[Long]
    )(TweetCounter.apply _)

  implicit val tjReads: Reads[TweetJson] = (
    (JsPath \ "timestamp").read[Long] and
      (JsPath \ "period").read[Int] and
      (JsPath \ "values").read[Seq[TweetCounter]]
    )(TweetJson.apply _)



  override def receive: Actor.Receive = {
    case PersistenceMsg(Refresh3(), j: JsValue) => {
      Logger.info(s"received Refresh 3 seconds message.")

      db.withSession { implicit session: Session =>
        val tweetJson: JsResult[TweetJson] = j.validate[TweetJson]
        tweetJson match {
          case s: JsSuccess[TweetJson] => {
            val tj: TweetJson = s.get
            for(tc <- tj.tc) {
              Logger.info(s"timestamp = " + tj.timestamp + " stub = " + tc.stub + " value = " + tc.tweets)
              val qs:QueryString = QueryString(0, tc.stub)
              val id = QueryStringDAO.findOrInsert(qs)
              val ts:ThreeSec = ThreeSec(new DateTime(tj.timestamp), id, tc.tweets)
              ThreeSecDAO.insert(ts)
            }
          }
          case e: JsError => println("Errors: " + JsError.toFlatJson(e).toString())
        }
      }
    }

    case PersistenceMsg(Refresh30(), j: JsValue) => {
      Logger.info(s"received Refresh 30 seconds message.")
    }

    case PersistenceMsg(Refresh300(), j: JsValue) => {
      Logger.info(s"received Refresh 300 seconds message.")
    }

    case PersistenceMsg(Refresh1800(), j: JsValue) => {
      Logger.info(s"received Refresh 1800 seconds message.")
    }

    case PersistenceMsg(Refresh10800(), j: JsValue) => {
      Logger.info(s"received Refresh 10800 seconds message.")
    }
  }
}
