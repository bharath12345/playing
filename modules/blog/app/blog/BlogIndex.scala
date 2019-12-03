package blog

import org.joda.time.DateTime
import play.api.{Environment, Logging}

import scala.collection.immutable.HashMap

/**
 * Created by bharadwaj on 09/04/14.
 */
object BlogIndex extends Posts with Logging {

  var dateMap = new HashMap[String, String]
  var contentMap = new HashMap[String, String]
  var fileMap = new HashMap[String, String]
  var sortedTitleList = List[String]()
  var pathMap = new HashMap[String, String]

  def setupIndexPage(env: Environment) = {
    var titleMap = new HashMap[Long, String]
    for (file <- posts) {
      val lines = fileContent(env, "conf/posts/" + file)
      val header = lines.takeWhile(line => !line.equals("}}}")).toSeq
      val content = lines.dropWhile(line => !line.equals("}}}")).drop(1).dropWhile(line => line.length == 0)

      content.takeWhile(line => line.length != 0).headOption.foreach { cont: String =>
        val excerpt = pegdown.markdownToHtml(cont)
        logger.debug(excerpt)

        val title = getLine(header, "\"title\"")
        logger.info("title = " + title + " for file = " + file)
        val date = getLine(header, "\"date\"")

        val ymd = date.split("-")
        val dt = new DateTime(ymd(2).toInt, ymd(0).toInt, ymd(1).toInt, 0, 0, 0)
        logger.info("date = " + dt)

        dateMap += (title -> date)
        titleMap += (dt.getMillis -> title)
        fileMap += (title -> file.replace(".md", ""))
        contentMap += (title -> excerpt)
        pathMap += (title -> ("post/" + file.replace(".md", "")))
      }
    }

    sortedTitleList = titleMap.toList.sortWith(_._1 > _._1).map(_._2) // sort by date
    logger.info(s"$sortedTitleList")
  }
}
