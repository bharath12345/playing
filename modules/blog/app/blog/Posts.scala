package blog

import org.pegdown.PegDownProcessor
import play.api.{Environment, Logging}

/**
 * Created by bharadwaj on 09/04/14.
 */
trait Posts extends Configuration with Logging {

  val pegdown = new PegDownProcessor

  val posts = List(
    "play2-on-jboss-wildfly.md",
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

  val blogIndex: String = "blog"
  val postType: String = "post"

  val titleField: String = "title"
  val subheadingField: String = "subheading"
  val tagsField: String = "tags"
  val categoryField: String = "category"
  val dateField: String = "date"
  val descriptionField: String = "description"
  val contentField: String = "content"
  val scoreField: String = "score"
  val urlField: String = "url"
  val fragmentsField: String = "fragments"
  val searchField: String = "search"


  /**
   *
   * @param file
   * @return
   */
  def fileContent(env: Environment, file: String): Seq[String] = {
    val relativepath = s"modules/blog/$file"
    val existingFile = env.getExistingFile(relativepath)
    logger.info(s"existingFile = $existingFile")
    existingFile match {
      case Some(x) =>
        val source = scala.io.Source.fromFile(x)
        val lines = source.getLines().toSeq
        source.close()
        lines

      case None =>
        logger.error(s"file not found: $relativepath")
        Seq()
    }
  }

  /**
   *
   * @param lines
   * @param search
   * @return
   */
  def getLine(lines: Seq[String], search: String): Option[String] = {
    val ol: Option[String] = lines.find(line => line.contains(search))
    logger.debug("ol = " + ol)
    ol.flatMap { l =>
      val lrhs: Array[String] = l.replaceAll("\"", "").replaceAll(",", "").trim.split(":")
      lrhs.headOption.flatMap { x =>
        logger.debug("lrhs 0 = " + lrhs(0) + " 1 = " + lrhs(1))
        val lhs = x.trim
        logger.debug("lhs = " + lhs)
        if (lrhs.length == 2)
          Option(lrhs(1).trim)
        else
          None
      }
    }
  }
}
