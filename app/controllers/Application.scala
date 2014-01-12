package controllers

import play.api._
import play.api.mvc._
import scala.collection.mutable.ListBuffer
import play.api.Play.current
import org.joda.time.DateTime
import scala.collection.immutable.HashMap
import scala.io.Source
import org.pegdown.PegDownProcessor

object Application extends Controller {

  val pegdown = new PegDownProcessor

  def index = Action {

    def getLine(lines: Seq[String], search: String): String = {
      val l = lines.filter(line => line.contains(search))(0)
      val lrhs = l.replaceAll("\"","").replaceAll(",","").trim.split(":")
      val lhs = lrhs(0).trim;val rhs = lrhs(1).trim
      //println(s"lhs = $lhs rhs = $rhs")
      rhs
    }

    val posts = Play.getFile("posts")

    var titleMap = new HashMap[Long, String]
    var dateMap = new HashMap[String, String]
    var contentMap = new HashMap[String, String]
    var fileList = new HashMap[String, String]

    for(file <- posts.listFiles) {
      val lines = Source.fromFile(file).getLines().toSeq
      val header = lines.takeWhile( line => !line.equals("}}}"))
      val content = lines.dropWhile( line => !line.equals("}}}")).drop(1).dropWhile(line => line.length == 0)
      val excerpt = pegdown.markdownToHtml(content.takeWhile( line => line.length != 0)(0))
      //println(excerpt)

      val title      = getLine(header, "\"title\"")
      val date       = getLine(header, "\"date\"")

      dateMap = dateMap + (title -> date)
      val ymd = date.split("-")

      val dt = new DateTime(ymd(2).toInt, ymd(0).toInt, ymd(1).toInt, 0, 0, 0)
      //println("date = " + dt)
      titleMap = titleMap + (dt.getMillis -> title)

      fileList = fileList + (title -> file.getName.replace(".md",""))
      contentMap = contentMap + (title -> excerpt)
    }

    val sorted = titleMap.toList.sortWith(_._1 > _._1) // sort by date
    //println(sorted)

    Ok(views.html.index(sorted.map(_._2))(fileList)(dateMap)(contentMap))
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