package controllers

import play.api._
import play.api.mvc._
import play.api.Play.current
import org.joda.time.DateTime
import scala.collection.immutable.{HashSet, HashMap}
import scala.io.Source
import org.pegdown.PegDownProcessor
import java.io.IOException

object Application extends Controller {

  val pegdown = new PegDownProcessor

  val posts = List("algorithms-course-i-with-prof-sidgewick-on-coursera.md",
    "build-dojo-1819-with-maven.md",
    "concurrency-on-the-jvm.md",
    "effective-java.md",
    "few-days-with-apache-cassandra.md",
    "folding-it-the-right-way.md",
    "is-there-a-collection-challenge-at-the-data-center.md",
    "java-and-jvm-7-slides-from-a-quick-talk.md",
    "programming-is-hard-to-manage.md",
    "real-time-dashboard-with-camel-activemq-dojo-on-jboss-using-jms--websocket.md",
    "scala-projects-in-the-making.md",
    "the-bleeding-edge-of-an-application.md",
    "the-visual-display-of-quantitative-information.md",
    "topology-graphs-with-d3-and-jsplumb.md",
    "weekend-well-spent-with-jsfoo-nodejs.md",
    "why-learn-scala.md"
  ).map( name => "public/posts/" + name)

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
    //val posts = Play.resource("public/posts").map(_.toURI).map(new java.io.File(_)).get

    /*val x = Play.resourceAsStream("public/posts") match {
      case Some(is) => scala.io.Source.fromInputStream(is).getLines().toList
      case _ => throw new IOException("file not found: public/posts")
    }
    println(x)*/

    var titleMap = new HashMap[Long, String]
    var dateMap = new HashMap[String, String]
    var contentMap = new HashMap[String, String]
    var fileList = new HashMap[String, String]

    for (file <- posts) {
      val lines = Play.resourceAsStream(file) match {
        case Some(is) => scala.io.Source.fromInputStream(is, "iso-8859-1").getLines().toSeq
        case _ => throw new IOException("file not found: " + file)
      }

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
      fileList += (title -> file.replace(".md", ""))
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
    val url = "public/posts/" + id + ".md"
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
    val posts = Play.getFile("public/posts")
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
    val posts = Play.getFile("public/posts")
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
    val posts = Play.getFile("public/posts")
    var categorySet = new HashSet[String]

    for (file <- posts.listFiles) {
      val lines = Source.fromFile(file).getLines().toSeq
      val header = lines.takeWhile(line => !line.equals("}}}"))
      val category = getLine(header, "\"category\"").filter(!"\"".contains(_)).trim
      println("category = " + category)
      categorySet += category
    }

    Ok(views.html.categories(categorySet))
  }

  /**
   *
   * @param id
   * @return
   */
  def category(id: String) = Action {
    val posts = Play.getFile("public/posts")
    var titleSet = new HashSet[String]
    var fileList = new HashMap[String, String]
    var dateMap = new HashMap[String, String]

    for (file <- posts.listFiles) {
      val lines = Source.fromFile(file).getLines().toSeq
      val header = lines.takeWhile(line => !line.equals("}}}"))
      val category = getLine(header, "\"category\"").filter(!"\"".contains(_)).trim
      val date = getLine(header, "\"date\"")

      if (category.equals(id)) {
        val title = getLine(header, "\"title\"")
        titleSet += title
        fileList += (title -> file.getName.replace(".md", ""))
        dateMap += (title -> date)
      }
    }

    Ok(views.html.category(id)(titleSet)(fileList)(dateMap))
  }

  /**
   *
   * @return
   */
  def toc = Action {
    val posts = Play.getFile("public/posts")

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