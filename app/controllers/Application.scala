package controllers

import play.api._
import play.api.mvc._
import scala.collection.mutable.ListBuffer
import play.api.Play.current
import org.joda.time.DateTime
import scala.collection.immutable.{HashSet, HashMap}
import scala.io.Source
import org.pegdown.PegDownProcessor

object Application extends Controller {

  val pegdown = new PegDownProcessor

  /**
   *
   * @param lines
   * @param search
   * @return
   */
  def getLine(lines: Seq[String], search: String): String = {
    val l = lines.filter(line => line.contains(search))(0)
    val lrhs = l.replaceAll("\"", "").replaceAll(",", "").trim.split(":")
    val lhs = lrhs(0).trim
    if (lrhs.length == 2)
      lrhs(1).trim
    else
      ""
  }

  /**
   *
   * @return
   */
  def index = Action {
    val posts = Play.getFile("posts")

    var titleMap = new HashMap[Long, String]
    var dateMap = new HashMap[String, String]
    var contentMap = new HashMap[String, String]
    var fileList = new HashMap[String, String]

    //println("resource = " + Play.resource("public/images"))

    for (file <- posts.listFiles) {
      val lines = Source.fromFile(file).getLines().toSeq
      val header = lines.takeWhile(line => !line.equals("}}}"))
      val content = lines.dropWhile(line => !line.equals("}}}")).drop(1).dropWhile(line => line.length == 0)
      val excerpt = pegdown.markdownToHtml(content.takeWhile(line => line.length != 0)(0))
      //println(excerpt)

      val title = getLine(header, "\"title\"")
      val date = getLine(header, "\"date\"")

      val ymd = date.split("-")
      val dt = new DateTime(ymd(2).toInt, ymd(0).toInt, ymd(1).toInt, 0, 0, 0)
      //println("date = " + dt)

      dateMap += (title -> date)
      titleMap += (dt.getMillis -> title)
      fileList += (title -> file.getName.replace(".md", ""))
      contentMap += (title -> excerpt)
    }

    val sorted = titleMap.toList.sortWith(_._1 > _._1) // sort by date
    //println(sorted)

    Ok(views.html.index(sorted.map(_._2))(fileList)(dateMap)(contentMap))
  }

  /**
   *
   * @param id
   * @return
   */
  def blog(id: String) = Action {
    val url = "posts/" + id + ".md"
    val post = Play.getFile(url)

    val lines = Source.fromFile(post).getLines().toSeq
    val header = lines.takeWhile(line => !line.equals("}}}"))
    val content = pegdown.markdownToHtml(lines.dropWhile(line => !line.equals("}}}")).drop(1).mkString("\n"))
    //println(content)

    val title = getLine(header, "\"title\"")
    val date = getLine(header, "\"date\"")
    val subheading = getLine(header, "\"subheading\"")
    val toc = getLine(header, "\"toc\"").toBoolean

    Ok(views.html.blog(id.replaceAll(".md", ""))(title)(content)(date)(subheading)(toc))
  }

  /**
   *
   * @return
   */
  def tags = Action {

    val posts = Play.getFile("posts")
    //var tagMap = new HashMap[String, ListBuffer[String]]
    var tagSet = new HashSet[String]

    for (file <- posts.listFiles) {
      val lines = Source.fromFile(file).getLines().toSeq
      val header = lines.takeWhile(line => !line.equals("}}}"))
      val tagLine = getLine(header, "\"tags\"").filter(!"[]\"".contains(_))

      val tags = tagLine.split(" ")
      //println("tags = " + tags.mkString(","))

      for {tag <- tags; if (tag.length > 0)} {
        println("tag = " + tag)
        tagSet += tag.trim
      }

    }

    Ok(views.html.tags(tagSet))
  }

  /**
   *
   * @param id
   * @return
   */
  def tag(id: String) = Action {
    val posts = Play.getFile("posts")
    var titleSet = new HashSet[String]
    var fileList = new HashMap[String, String]
    var dateMap = new HashMap[String, String]

    for (file <- posts.listFiles) {
      val lines = Source.fromFile(file).getLines().toSeq
      val header = lines.takeWhile(line => !line.equals("}}}"))
      val tagLine = getLine(header, "\"tags\"").filter(!"[]\"".contains(_))
      val date = getLine(header, "\"date\"")

      if (tagLine.contains(id)) {
        val title = getLine(header, "\"title\"")
        titleSet += title
        fileList += (title -> file.getName.replace(".md", ""))
        dateMap += (title -> date)
      }
    }

    Ok(views.html.tag(id)(titleSet)(fileList)(dateMap))
  }

  /**
   *
   * @return
   */
  def categories = Action {
    Ok(views.html.categories("categories page!"))
  }

  /**
   *
   * @return
   */
  def toc = Action {
    val posts = Play.getFile("posts")

    var titleMap = new HashMap[Long, String]
    var dateMap = new HashMap[String, String]
    var fileList = new HashMap[String, String]

    for (file <- posts.listFiles) {
      val lines = Source.fromFile(file).getLines().toSeq
      val header = lines.takeWhile(line => !line.equals("}}}"))

      val title = getLine(header, "\"title\"")
      val date = getLine(header, "\"date\"")

      val ymd = date.split("-")
      val dt = new DateTime(ymd(2).toInt, ymd(0).toInt, ymd(1).toInt, 0, 0, 0)
      //println("date = " + dt)

      dateMap += (title -> date)
      titleMap += (dt.getMillis -> title)
      fileList += (title -> ("post/" + file.getName.replace(".md", "")))
    }
    val sorted = titleMap.toList.sortWith(_._1 > _._1) // sort by date

    Ok(views.html.toc(sorted.map(_._2))(fileList)(dateMap))
  }

  /**
   *
   * @return
   */
  def search = Action {
    Ok(views.html.search("search page!"))
  }

}