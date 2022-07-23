package blog

import play.api.{Environment, Logging}
import org.pegdown.PegDownProcessor

import java.io.File

/**
 * Created by bharadwaj on 09/04/14.
 */
trait Posts extends Logging {

  val pegdown = new PegDownProcessor

  val posts = List(
    "2013-07-10-the-visual-display-of-quantitative-information.md",
    "2013-07-11-few-days-with-apache-cassandra.md",
    "2013-07-18-build-dojo-1819-with-maven.md",
    "2013-07-31-java-and-jvm-7-slides-from-a-quick-talk.md",
    "2013-08-01-real-time-dashboard-with-camel-activemq-dojo-on-jboss-using-jms--websocket.md",
    "2013-08-06-is-there-a-collection-challenge-at-the-data-center.md",
    "2013-08-22-effective-java.md",
    "2013-09-01-topology-graphs-with-d3-and-jsplumb.md",
    "2013-09-11-the-bleeding-edge-of-an-application.md",
    "2013-09-24-weekend-well-spent-with-jsfoo-nodejs.md",
    "2013-10-08-algorithms-course-i-with-prof-sidgewick-on-coursera.md",
    "2013-10-31-folding-it-the-right-way.md",
    "2013-11-14-concurrency-on-the-jvm.md",
    "2013-11-26-programming-is-hard-to-manage.md",
    "2013-12-11-why-learn-scala.md",
    "2013-12-25-scala-projects-in-the-making.md",
    "2014-01-16-application-developers-view-postgresql-vs-mysql.md",
    "2014-01-29-streaming-twitter-on-play--spray-scala-app.md",
    "2014-02-23-computing-laws-theorems-and-aphorisms.md",
    "2014-02-27-my-scala-application---twitter-volume-grapher-for-indian-election-personalities.md",
    "2014-03-13-play2-on-jboss-wildfly.md",
    "2014-04-14-code-retreat.md",
    "2014-05-12-run-machine-learning-assignments-on-a-laptops-spark-cluster.md",
    "2014-06-28-experiments-with-xml-xpath-libraries-on-jvm.md",
    "2014-10-17-functional-conference-random-notes.md",
    "2021-02-15-scalajs-dsl-parsercombinator.md"
  ).reverse

  val blogIndex: String = "blog"
  val postType: String = "post"

  val titleField: String = "title"
  val subheadingField: String = "subheading"
  val tagsField: String = "tags"
  val categoryField: String = "tag"
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

    def readFile(f: File): Seq[String] = {
      val source = scala.io.Source.fromFile(f)
      val lines = source.getLines().toSeq
      source.close()
      lines
    }

    def getFile(relativepath: String): Option[File] = {
      val existingFile = env.getExistingFile(relativepath)
      logger.info(s"existingFile = $existingFile")
      existingFile
    }

    getFile(s"modules/blog/posts/$file") match {
      case Some(x) => readFile(x)
      case None =>
        getFile(s"posts/$file") match {
          case Some(y) => readFile(y)
          case None =>
            logger.error(s"file not found: $file")
            Seq()
        }
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
    logger.info("ol = " + ol)
    ol.flatMap { l =>
      val lrhs: Array[String] = l.replaceAll("\"", "").replaceAll(",", "").trim.split(":")
      logger.info(s"lrhs = ${lrhs.toList}")
      if (lrhs.length == 2)
        Option(lrhs(1).trim)
      else
        None
      /*lrhs.headOption.flatMap { x =>
        logger.info("lrhs 0 = " + lrhs(0) + " 1 = " + lrhs(1))
        val lhs = x.trim
        logger.info("lhs = " + lhs)

      }*/
    }
  }
}
