package blog

import blog.BlogGlobal.BlogPostContent
import play.api.{Environment, Logging}

/** Created by bharadwaj on 09/04/14.
  */
object BlogPost extends Posts with Logging {

  def setupPosts(env: Environment, file: String): Option[BlogPostContent] = {
    val lines = fileContent(env, s"$file.md")
    val header: Seq[String] = lines.takeWhile(line => !line.equals("}}}")).toSeq
    val result = for {
      title <- getLine(header, "\"title\"")
      date <- getLine(header, "\"date\"")
    } yield {
      val subheading = getLine(header, "\"subheading\"")
      val toc = getLine(header, "\"toc\"").map(_.toBoolean)
      val content: String = pegdown.markdownToHtml(
        lines.dropWhile(line => !line.equals("}}}")).drop(1).mkString("\n")
      )
      BlogPostContent(title, content, date, subheading, toc)
    }
    logger.debug(s"$result")
    result
  }

}
