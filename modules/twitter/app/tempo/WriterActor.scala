package tempo

import akka.actor.Actor
import models.twitter._
import models._
import play.api.libs.json.{JsResult, JsValue}
import play.api.Logger
import org.joda.time.DateTime
import _root_.twitter.Configuration
import scala.slick.jdbc.JdbcBackend._
import models.FlushOneHour
import models.Refresh30
import models.FlushOneDay
import models.FlushOneMonth
import models.Refresh1800
import models.twitter.ThreeSec
import models.twitter.PersistenceMsg
import models.Refresh3
import models.Refresh10800
import models.Refresh300
import models.FlushThreeHours
import models.FlushOneWeek
import java.util
import com.tempodb.models.{MultiPoint, MultiKeyPoint, DataPoint}

/**
 * Created by bharadwaj on 27/03/14.
 */
class WriterActor() extends Actor with Configuration with TweetJson {

  def persist(j: JsValue) = {
    val tss: Seq[ThreeSec] = db.withSession {
      implicit session: Session =>
        val tweetJson: JsResult[TweetJson] = validate(j)
        retrievePojo(tweetJson)
    }
    val datapoints: util.List[MultiPoint] = new util.ArrayList[MultiPoint]()
    for (ts <- tss) {
      datapoints.add(new MultiKeyPoint(QueryIdCache.get(ts.queryString).get, ts.dateTime, ts.count))
    }
    tempoClient.multiWrite(datapoints)
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
