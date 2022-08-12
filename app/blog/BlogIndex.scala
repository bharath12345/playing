package blog

import BlogGlobal.BlogIndexContent
import org.joda.time.DateTime
import play.api.{Environment, Logging}

/** Created by bharadwaj on 09/04/14.
  */
object BlogIndex extends Posts with Logging {

  def setupIndexPage(
      env: Environment,
      page: Int
  ): (List[(String, BlogIndexContent)], Int) = {
    val fposts: List[String] =
      if (page == 0) posts.take(5)
      else posts.slice(page * 5, page * 5 + 5)

    val result: List[(String, BlogIndexContent)] = for {
      file <- fposts
      lines = fileContent(env, file)
      header = lines.takeWhile(line => !line.equals("}}}"))
      _ = logger.info(s"header = $header")
      content = lines
        .dropWhile(line => !line.equals("}}}"))
        .drop(1)
        .dropWhile(line => line.length == 0)
      _ = logger.info(s"content = ${content.take(20)}")
      cont = content.takeWhile(line => line.length != 0).mkString
      excerpt = pegdown.markdownToHtml(cont)
      _ = logger.debug(excerpt)
      title <- getLine(header, "\"title\"")
      _ = logger.info("title = " + title + " for file = " + file)
      date <- getLine(header, "\"date\"")
    } yield {
      val ymd = date.split("-")
      val dt = new DateTime(ymd(2).toInt, ymd(1).toInt, ymd(0).toInt, 0, 0, 0)
      logger.info("date = " + dt)
      file.replace(".md", "") -> BlogIndexContent(
        date,
        excerpt,
        title,
        "post/" + file.replace(".md", "")
      )
    }

    logger.info(s"$result")
    (result, posts.size)
  }
}
