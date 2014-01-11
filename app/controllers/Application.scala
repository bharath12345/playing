package controllers

import play.api._
import play.api.mvc._
import scala.collection.mutable.ListBuffer
import play.api.Play.current

object Application extends Controller {

  def index = Action {
    val posts = Play.getFile("posts")
    val fileList = new ListBuffer[String]()
    for(file <- posts.listFiles) {
      fileList += file.getName
    }
    Ok(views.html.index(fileList.toList))
  }

  def blog(id: String) = Action {
    Ok(views.html.blog(id))
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