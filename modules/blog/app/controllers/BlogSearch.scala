package controllers.blog

import play.api.mvc.{Action, Controller}
import elasticsearch.BlogIndexer

/**
 * Created by bharadwaj on 09/04/14.
 */
object BlogSearch extends Controller {

  def search = Action {
    Ok("num of hits = " + BlogIndexer.searchOne)
  }

  def searchQuery(q: String) = Action {
    Ok(BlogIndexer.searchText(q))
  }
}
