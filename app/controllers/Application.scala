package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def tags = Action {
    Ok(views.html.tags("tag page!"))
  }

  def categories = Action {
    Ok(views.html.categories("categories page!"))
  }

  def toc = Action {
    Ok(views.html.toc("toc page!"))
  }

  def search = Action {
    Ok(views.html.search("search page!"))
  }

}