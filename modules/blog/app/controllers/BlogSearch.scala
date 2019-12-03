package controllers.blog

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}

/**
 * Created by bharadwaj on 09/04/14.
 */
@Singleton
class BlogSearch @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def search = Action {
    implicit request =>
      //val searchUrl = controllers.blog.routes.BlogSearch.search.absoluteURL()
      Ok("ToDo")
  }

  def searchCount = Action {
    Ok("num of hits = " /*BlogSearcher.searchCount*/)
  }

  //def searchQuery(q: String, c: Int, p: Int) = Action.async {
  //  BlogSearcher.searchText(q, c, p).map(searchJson => Ok(searchJson))
  //}
}
