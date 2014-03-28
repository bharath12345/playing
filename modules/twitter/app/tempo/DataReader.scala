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

  def last2Minutes(key: String): DataSet = tempoClient.readKey(getKey(3)(key), (new DateTime()).minusMinutes(2), new DateTime())

  def last15Minutes(key: String): DataSet = tempoClient.readKey(getKey(3)(key), (new DateTime()).minusMinutes(15), new DateTime())

}
