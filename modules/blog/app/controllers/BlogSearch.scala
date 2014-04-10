package controllers.blog

import play.api.mvc.{Action, Controller}
import elasticsearch.{BlogSearcher, BlogIndexer}
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by bharadwaj on 09/04/14.
 */
object BlogSearch extends Controller {

  def search = Action {
    Ok(views.html.search())
  }

  def searchCount = Action {
    Ok("num of hits = " + BlogSearcher.searchCount)
  }

  def searchQuery(q: String, c: Int, p: Int) = Action.async {
    BlogSearcher.searchText(q, c, p).map(searchJson => Ok(searchJson))
  }
}
