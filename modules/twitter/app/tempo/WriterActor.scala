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
class WriterActor() extends Actor with Configuration with TweetJson with TempoKeyHelper {

  def persist(j: JsValue, getKey: String => String) = {
    val tss: Seq[ThreeSec] = db.withSession {
      implicit session: Session =>
        val tweetJson: JsResult[TweetJson] = validate(j)
        retrievePojo(tweetJson)
    }
    val datapoints: util.List[MultiPoint] = new util.ArrayList[MultiPoint]()
    for (ts <- tss) {
      val key = getKey(QueryIdCache.get(ts.queryString).get)
      Logger.info(s"using key = " + key)
      datapoints.add(new MultiKeyPoint(key, ts.dateTime, ts.count))
    }
    tempoClient.multiWrite(datapoints)
  }

  def queryStrings: List[QueryString] = {
    val qslist: List[QueryString] = db.withSession {
      implicit session: Session =>
        QueryStringDAO.findAll
    }
    qslist
  }

  override def receive: Actor.Receive = {
    case PersistenceMsg(r: Refresh3, j: JsValue) => {
      Logger.info(s"received Refresh 3 seconds message.")
      persist(j, getKey(3))
    }

    case PersistenceMsg(r: Refresh30, j: JsValue) => {
      Logger.info(s"received Refresh 30 seconds message.")
      persist(j, getKey(30))
    }

    case PersistenceMsg(r: Refresh300, j: JsValue) => {
      Logger.info(s"received Refresh 300 seconds message.")
      persist(j, getKey(300))
    }

    case PersistenceMsg(r: Refresh1800, j: JsValue) => {
      Logger.info(s"received Refresh 1800 seconds message.")
      persist(j, getKey(1800))
    }

    case PersistenceMsg(r: Refresh10800, j: JsValue) => {
      Logger.info(s"received Refresh 10800 seconds message.")
      persist(j, getKey(10800))
    }

    case f: FlushOneHour => {
      val qslist: List[QueryString] = queryStrings
      for(qs <- qslist) {
        // filter for all data between one day ago and one hour ago and delete them
        tempoClient.deleteKey(qs.queryString, (new DateTime()).minusDays(1), (new DateTime()).minusHours(1))
      }
    }

    case FlushThreeHours() => {
      val qslist: List[QueryString] = queryStrings
      for(qs <- qslist) {
        // filter for all data between one day ago and 3 hours ago and delete them
        tempoClient.deleteKey(qs.queryString, (new DateTime()).minusDays(1), (new DateTime()).minusHours(3))
      }
    }

    case FlushOneDay() => {
      val qslist: List[QueryString] = queryStrings
      for(qs <- qslist) {
        // filter for all data between one month ago and one day ago and delete them
        tempoClient.deleteKey(qs.queryString, (new DateTime()).minusMonths(1), (new DateTime()).minusDays(1))
      }
    }

    case FlushOneWeek() => {
      val qslist: List[QueryString] = queryStrings
      for(qs <- qslist) {
        // filter for all data between one month ago and one week ago and delete them
        tempoClient.deleteKey(qs.queryString, (new DateTime()).minusMonths(1), (new DateTime()).minusWeeks(1))
      }
    }

    case FlushOneMonth() => {
      val qslist: List[QueryString] = queryStrings
      for(qs <- qslist) {
        // filter for all data between 3 months ago and 1 month ago and delete them
        tempoClient.deleteKey(qs.queryString, (new DateTime()).minusMonths(3), (new DateTime()).minusMonths(1))
      }
    }

  }
}
