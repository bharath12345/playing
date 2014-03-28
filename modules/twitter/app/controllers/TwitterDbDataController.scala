package controllers.twitter

import play.api.mvc.{Action, Controller}
import _root_.twitter.Configuration
import com.tempodb.models.{DataPoint, DataSet, Filter, Series}
import org.joda.time.DateTime
import java.util
import play.api.Logger
import play.api.libs.json.{Writes, Json, JsObject, JsValue}
import models.twitter.{QueryStringDAO, QueryString}
import scala.slick.jdbc.JdbcBackend._

/**
 * Created by bharadwaj on 27/03/14.
 */
object TwitterDbDataController extends Controller with Configuration {

  case class Summary(name: String, sum: Long, mean: Float, max: Int, count: Long)
  case class SummaryList(summaries: Seq[Summary])

  case class RawDataPoint(time: Long, value: Int)
  case class RawData(name: String, points: Seq[RawDataPoint])
  case class RawAll(rd: Seq[RawData])
  
  implicit val summaryWrites = new Writes[Summary] {
    def writes(summary: Summary) = Json.obj(
      "name" -> summary.name,
      "sum"  -> summary.sum,
      "mean" -> summary.mean,
      "max"  -> summary.max,
      "count"-> summary.count
    )
  }
  
  implicit val summaryListWrites = new Writes[SummaryList] {
    def writes(sl: SummaryList) = Json.obj (
      "summary" -> sl.summaries
    )
  }

  implicit val rawDataPoint = new Writes[RawDataPoint] {
    def writes(rdp: RawDataPoint) = Json.obj(
      "time" -> rdp.time,
      "value"-> rdp.value
    )
  }

  implicit val rawData = new Writes[RawData] {
    def writes(rd: RawData) = Json.obj(
      "name" -> rd.name,
      "points" -> rd.points
    )
  }

  implicit val rawAll = new Writes[RawAll] {
    def writes(ra: RawAll) = Json.obj(
      "all" -> ra.rd
    )
  }

  def lastDayTempoDb() = Action {
    val filter: Filter = new Filter()
    filter.addKey("modi")
    filter.addKey("india")
    filter.addKey("kejri")
    filter.addKey("rahul")

    val datasets: util.List[DataSet] = tempoClient.read((new DateTime()).minusDays(1), new DateTime(), filter);
    val diterator = datasets.iterator()
    var slist: scala.collection.mutable.Seq[Summary] = scala.collection.mutable.Seq[Summary]()

    while(diterator.hasNext) {
      val ds: DataSet = diterator.next()
      val series: Series = ds.getSeries
      val summary: util.Map[String,Number] = ds.getSummary
      val s: Summary = Summary(series.getKey(), summary.get("sum").longValue(), summary.get("mean").floatValue(),
        summary.get("max").intValue(), summary.get("count").intValue())
      slist = slist :+ s
    }
    val jsonMsg: JsValue = Json.toJson(slist)
    Ok(jsonMsg)
  }

  private def getLastHourRawData(key: String)  = {
    val dataset: DataSet = tempoClient.readKey(key, (new DateTime()).minusMinutes(2), new DateTime())
    var rawDataPoints: scala.collection.mutable.Seq[RawDataPoint] = scala.collection.mutable.Seq[RawDataPoint]()
    val diterator = dataset.getData.iterator()
    while(diterator.hasNext) {
      val dp: DataPoint = diterator.next()
      val rdp: RawDataPoint = RawDataPoint(dp.getTimestamp.getMillis, dp.getValue.intValue())
      rawDataPoints = rawDataPoints :+ rdp
    }
    rawDataPoints
  }

  def lastHourThreeSecData(key: String) = Action {
    val rawDataPoints = getLastHourRawData(key)
    val rd: RawData = RawData(key, rawDataPoints)
    val jsonMsg: JsValue = Json.toJson(rd)
    Ok(jsonMsg)
  }

  def lastHourThreeSecDataAll = Action {
    val qslist: List[QueryString] = db.withSession {
      implicit session: Session =>
        QueryStringDAO.findAll
    }
    var rawAll: scala.collection.mutable.Seq[RawData] = scala.collection.mutable.Seq[RawData]()
    for(qs <- qslist) {
      val rawDataPoints = getLastHourRawData(qs.queryString)
      val rd: RawData = RawData(qs.queryString, rawDataPoints)
      rawAll = rawAll :+ rd
    }
    val jsonMsg: JsValue = Json.toJson(rawAll)
    Ok(jsonMsg)
  }
}
