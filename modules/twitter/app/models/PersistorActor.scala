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

class PersistorActor() extends Actor with Configuration with TweetJson {

  /*
    Json Msg: {"timestamp":1395763755674,"period":0,"values":[{"stub":"kejri","tweets":4},{"stub":"rahul","tweets":1},{"stub":"modi","tweets":6},{"stub":"india","tweets":6}]}
   */

  def persist(j: JsValue) = {
    db.withSession { implicit session: Session =>
      val tweetJson: JsResult[TweetJson] = validate(j)
      val tss:Seq[ThreeSec] = retrievePojo(tweetJson)
      for(ts <- tss) {
        ThreeSecDAO.insert(ts)
      }
    }
  }

  override def receive: Actor.Receive = {
    case PersistenceMsg(Refresh3(), j: JsValue) => {
      Logger.info(s"received Refresh 3 seconds message.")
      persist(j)
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

    case f: FlushOneHour => {

      val onehourago: DateTime = (new DateTime()).minusSeconds(f.flushDuration)
      db.withSession { implicit session: Session =>
        ThreeSecDAO.deleteLashHour(onehourago)
      }
    }

    case FlushThreeHours() => {

    }

    case FlushOneDay() => {

    }

    case FlushOneWeek() => {

    }

    case FlushOneMonth() => {

    }
  }
}
