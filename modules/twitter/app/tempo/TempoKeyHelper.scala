package tempo

/**
 * Created by bharadwaj on 28/03/14.
 */
trait TempoKeyHelper {

  def getKey(refresh: String)(qs: String): String = "refresh:" + refresh + "." + qs + ".1"

}
