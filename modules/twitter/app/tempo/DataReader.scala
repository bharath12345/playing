package tempo

import _root_.twitter.Configuration
import java.util
import com.tempodb.models.DataSet
import org.joda.time.DateTime
import com.tempodb.models.{DataPoint, DataSet, Filter, Series}

/**
 * Created by bharadwaj on 28/03/14.
 */
object DataReader extends Configuration with TempoKeyHelper {

  def lastDayByFilter(fList: List[String]): util.List[DataSet] = {
    val filter: Filter = new Filter()
    for(f <- fList) {
      filter.addKey(getKey(3)(f))
    }
    tempoClient.read((new DateTime()).minusDays(1), new DateTime(), filter)
  }

  // 3 seconds for last 2 minutes => 40 data points
  def last2Minutes(key: String): DataSet = tempoClient.readKey(getKey(3)(key), (new DateTime()).minusMinutes(2), new DateTime())

  // 30 seconds for last 20 minutes = 40 data points
  def last15Minutes(key: String): DataSet = tempoClient.readKey(getKey(30)(key), (new DateTime()).minusMinutes(20), new DateTime())

  // 5 minutes for last 200 minutes (or 3hr20min) = 40 data points
  def last200Minutes(key: String): DataSet = tempoClient.readKey(getKey(300)(key), (new DateTime()).minusMinutes(20).minusHours(3), new DateTime())

  // 30 minutes for last 1200 minutes (or 20hrs) = 40 data points
  def last1200Minutes(key: String): DataSet = tempoClient.readKey(getKey(1800)(key), (new DateTime()).minusHours(20), new DateTime())

  // 3 hours for last 7200 minutes (or 120hrs) = 40 data points
  def last7200Minutes(key: String): DataSet = tempoClient.readKey(getKey(10800)(key), (new DateTime()).minusHours(120), new DateTime())

}
