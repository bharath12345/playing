package controllers.blog

import play.api._
import play.api.mvc._
import play.api.Play.current
import org.joda.time.DateTime
import scala.collection.immutable.{HashSet, HashMap}
import org.pegdown.PegDownProcessor
import java.io.IOException

object BlogApplication extends Controller {

  val pegdown = new PegDownProcessor

  val posts = List(
    "my-scala-application---twitter-volume-grapher-for-indian-election-personalities.md",
    "computing-laws-theorems-and-aphorisms.md",
    "streaming-twitter-on-play--spray-scala-app.md",
    "application-developers-view-postgresql-vs-mysql.md",
    "algorithms-course-i-with-prof-sidgewick-on-coursera.md",
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
  )

  /**
   *
   * @param file
   * @return
   */
  def fileContent(file: String): Seq[String] = {
    Play.resourceAsStream(file) match {
      case Some(is) => scala.io.Source.fromInputStream(is, "iso-8859-1").getLines().toSeq
      case _ => throw new IOException("file not found: " + file)
    }
  }

  /**
   *
   * @param lines
   * @param search
   * @return
   */
  def getLine(lines: Seq[String], search: String): String = {
    val l = lines.filter(line => line.contains(search))(0)
    //println("l = " + l)
    val lrhs = l.replaceAll("\"", "").replaceAll(",", "").trim.split(":")
    //println("lrhs 0 = " + lrhs(0) + " 1 = " + lrhs(1))
    val lhs = lrhs(0).trim
    //println("lhs = " + lhs)
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
    var titleMap = new HashMap[Long, String]
    var dateMap = new HashMap[String, String]
    var contentMap = new HashMap[String, String]
    var fileList = new HashMap[String, String]

    for (file <- posts) {
      val lines = fileContent("public/posts/" + file)
      val header = lines.takeWhile(line => !line.equals("}}}"))
      val content = lines.dropWhile(line => !line.equals("}}}")).drop(1).dropWhile(line => line.length == 0)
      val excerpt = pegdown.markdownToHtml(content.takeWhile(line => line.length != 0)(0))
      //println(excerpt)

      val title = getLine(header, "\"title\"")
      //println("title = " + title + " for file = " + file)
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
    val lines = fileContent("public/posts/" + id + ".md")

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
    var tagSet = new HashSet[String]
    for (file <- posts) {
      val lines = fileContent("public/posts/" + file)
      val header = lines.takeWhile(line => !line.equals("}}}"))
      val tagLine = getLine(header, "\"tags\"").filter(!"[]\"".contains(_))

      val tags = tagLine.split(" ")
      //println("tags = " + tags.mkString(","))

      for {tag <- tags; if (tag.length > 0)} {
        //println("tag = " + tag)
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
    var titleSet = new HashSet[String]
    var fileList = new HashMap[String, String]
    var dateMap = new HashMap[String, String]

    for (file <- posts) {
      val lines = fileContent("public/posts/" + file)
      val header = lines.takeWhile(line => !line.equals("}}}"))
      val tagLine = getLine(header, "\"tags\"").filter(!"[]\"".contains(_))
      val date = getLine(header, "\"date\"")

      if (tagLine.contains(id)) {
        val title = getLine(header, "\"title\"")
        titleSet += title
        fileList += (title -> file.replace(".md", ""))
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
    var categorySet = new HashSet[String]
    for (file <- posts) {
      val lines = fileContent("public/posts/" + file)
      val header = lines.takeWhile(line => !line.equals("}}}"))
      val category = getLine(header, "\"category\"").filter(!"\"".contains(_)).trim
      //println("category = " + category)
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
    var titleSet = new HashSet[String]
    var fileList = new HashMap[String, String]
    var dateMap = new HashMap[String, String]

    for (file <- posts) {
      val lines = fileContent("public/posts/" + file)
      val header = lines.takeWhile(line => !line.equals("}}}"))
      val category = getLine(header, "\"category\"").filter(!"\"".contains(_)).trim
      val date = getLine(header, "\"date\"")

      if (category.equals(id)) {
        val title = getLine(header, "\"title\"")
        titleSet += title
        fileList += (title -> file.replace(".md", ""))
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
    var titleMap = new HashMap[Long, String]
    var dateMap = new HashMap[String, String]
    var fileList = new HashMap[String, String]

    for (file <- posts) {
      val lines = fileContent("public/posts/" + file)
      val header = lines.takeWhile(line => !line.equals("}}}"))

      val title = getLine(header, "\"title\"")
      val date = getLine(header, "\"date\"")

      val ymd = date.split("-")
      val dt = new DateTime(ymd(2).toInt, ymd(0).toInt, ymd(1).toInt, 0, 0, 0)
      //println("date = " + dt)

      dateMap += (title -> date)
      titleMap += (dt.getMillis -> title)
      fileList += (title -> ("post/" + file.replace(".md", "")))
    }
    val sorted = titleMap.toList.sortWith(_._1 > _._1) // sort by date

    Ok(views.html.toc(sorted.map(_._2))(fileList)(dateMap))
  }

  /**
   *
   * @return
   */
  def search = TODO

}
