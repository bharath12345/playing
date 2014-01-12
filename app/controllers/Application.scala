package controllers

import play.api._
import play.api.mvc._
import scala.collection.mutable.ListBuffer
import play.api.Play.current
import org.joda.time.DateTime
import scala.collection.immutable.HashMap
import scala.io.Source

object Application extends Controller {

  def index = Action {

    def getLine(lines: Seq[String], search: String): String = {
      val l = lines.filter(line => line.contains(search))(0)
      val lrhs = l.replaceAll("\"","").replaceAll(",","").trim.split(":")
      val lhs = lrhs(0).trim;val rhs = lrhs(1).trim
      println(s"lhs = $lhs rhs = $rhs")
      rhs
    }

    val posts = Play.getFile("posts")

    val titleMap = new ListBuffer[String]
    var dateMap = new HashMap[String, String]
    var contentMap = new HashMap[String, Seq[String]]
    var fileList = new HashMap[String, String]

    for(file <- posts.listFiles) {
      val lines = Source.fromFile(file).getLines().toSeq
      val header = lines.takeWhile( line => !line.equals("}}}"))
      val content = lines.dropWhile( line => !line.equals("}}}")).drop(1).dropWhile(line => line.length == 0)
      val excerpt = content.takeWhile( line => line.length != 0)
      //for(h <- excerpt) println(h + "\n\n")

      val title      = getLine(header, "\"title\"")
      titleMap += title

      fileList = fileList + (title -> file.getName.replace(".md",""))

      val date       = getLine(header, "\"date\"")
      dateMap = dateMap + (title -> date)

      contentMap = contentMap + (title -> excerpt)
    }

    Ok(views.html.index(titleMap.toList)(fileList)(dateMap)(contentMap))
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