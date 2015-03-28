package elasticsearch

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by bharadwaj on 14/04/14.
 */
trait JestSearcher extends JestIndexer {

  def searchCount: Long = 0

  def searchText(queryString: String, count: Int, page: Int): Future[String] = {
    Future {
      "hello"
    }
  }

}
