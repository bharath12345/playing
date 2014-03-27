package controllers.twitter

import play.api.mvc.{Action, Controller}
import _root_.twitter.Configuration
import com.tempodb.models.{DataSet, Filter, Series}
import org.joda.time.DateTime
import java.util
import play.api.Logger

/**
 * Created by bharadwaj on 27/03/14.
 */
object TwitterDbDataController extends Controller with Configuration {

  def lastHourTempoDb(tag: String) = Action {
    val filter: Filter = new Filter()
    filter.addKey("modi");
    filter.addKey("india");
    val datasets: util.List[DataSet] = tempoClient.read((new DateTime()).minusHours(1), new DateTime(), filter);
    val diterator = datasets.iterator()
    var htmlmsg: String = ""
    while(diterator.hasNext) {
      val ds: DataSet = diterator.next()
      val series: Series = ds.getSeries
      val summary: util.Map[String,Number] = ds.getSummary

      val msg: String = "series name = " + series.getKey() +
        " sum = "  + summary.get("sum") +
        " mean = " + summary.get("mean") +
        " max = "  + summary.get("max") +
        " count = "+ summary.get("count")

      htmlmsg += "<p>"+ msg +"</p>"
      Logger.info(msg)
    }
    Ok(htmlmsg)
  }
}
