package blog

import blog.BlogGlobal.BlogIndexContent
import org.joda.time.DateTime
import play.api.{Environment, Logging}

import scala.collection.immutable.{HashMap, HashSet}

/**
 * Created by bharadwaj on 09/04/14.
 */
object BlogTag extends Posts with Logging {

  private def convertTagLine(t: String): Array[String] =
    for {
      tt <- t.split(" ")
      if tt.length > 0 && tt != "]" && tt != "["
    } yield tt.trim.toLowerCase

  def setupTags(env: Environment): Set[String] = {
    (for {
      file <- posts
      lines = fileContent(env, file)
      header = lines.takeWhile(line => !line.equals("}}}"))
      tagLine <- getLine(header, "\"tags\"").filter(!"[]\"".contains(_))
      tag = convertTagLine(tagLine)
    } yield {
      tag
    }).flatten.toSet
  }

  def listBlogsForTag(env: Environment, tag: String): Map[String, BlogIndexContent] = {
    val result: Map[String, BlogIndexContent] = (for {
      file <- posts
      lines = fileContent(env, file)
      header = lines.takeWhile(line => !line.equals("}}}")).toSeq
      tagLine <- getLine(header, "\"tags\"").filter(!"[]\"".contains(_))
      tags = convertTagLine(tagLine)
      if tags.contains(tag)
      content = lines.dropWhile(line => !line.equals("}}}")).drop(1).dropWhile(line => line.length == 0)
      cont <- content.takeWhile(line => line.length != 0).headOption
      excerpt = pegdown.markdownToHtml(cont)
      _ = logger.debug(excerpt)
      title <- getLine(header, "\"title\"")
      _ = logger.info("title = " + title + " for file = " + file)
      date <- getLine(header, "\"date\"")
    } yield {
      val ymd = date.split("-")
      val dt = new DateTime(ymd(2).toInt, ymd(0).toInt, ymd(1).toInt, 0, 0, 0)
      logger.info("date = " + dt)
      file.replace(".md", "") -> BlogIndexContent(date, excerpt, title, "post/" + file.replace(".md", ""))
    }).toMap
    logger.info(s"$result")
    result
  }

}
