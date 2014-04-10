package controllers.blog

import play.api.mvc.{Action, Controller}
import elasticsearch.BlogIndexer
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by bharadwaj on 09/04/14.
 */
object BlogSearch extends Controller {

  def search = Action {
    Ok("num of hits = " + BlogIndexer.searchOne)
  }

  def searchQuery(q: String) = Action.async {
    BlogIndexer.searchText(q).map(searchJson => Ok(searchJson))
  }
}
