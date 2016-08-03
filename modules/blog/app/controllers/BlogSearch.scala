package controllers.blog

import play.api.mvc.{Action, Controller}
//import elasticsearch.{BlogSearcher, BlogIndexer}
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by bharadwaj on 09/04/14.
 */
object BlogSearch extends Controller {

  def search = Action {
    implicit request =>
    //val searchUrl = controllers.blog.routes.BlogSearch.search.absoluteURL()
    Ok("ToDo")
  }

  def searchCount = Action {
    Ok("num of hits = "/*BlogSearcher.searchCount*/)
  }

  //def searchQuery(q: String, c: Int, p: Int) = Action.async {
  //  BlogSearcher.searchText(q, c, p).map(searchJson => Ok(searchJson))
  //}
}
