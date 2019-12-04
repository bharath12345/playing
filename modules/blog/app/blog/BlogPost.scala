package blog

import play.api.{Environment, Logging}

import scala.collection.immutable.HashMap

/**
 * Created by bharadwaj on 09/04/14.
 */
object BlogPost extends Posts with Logging {

  var title = new HashMap[String, String]
  var content = new HashMap[String, String]
  var date = new HashMap[String, String]
  var subheading = new HashMap[String, String]
  var toc = new HashMap[String, Boolean]

  def setupPosts(env: Environment) = {
    for (file <- posts) {
      val lines = fileContent(env, file)
      val header: Seq[String] = lines.takeWhile(line => !line.equals("}}}")).toSeq
      content += (file -> pegdown.markdownToHtml(lines.dropWhile(line => !line.equals("}}}")).drop(1).mkString("\n")))
      logger.debug(s"$content")

      getLine(header, "\"title\"").foreach(x => title += (file -> x))
      getLine(header, "\"date\"").foreach(x => date += (file -> x))
      getLine(header, "\"subheading\"").foreach(x => subheading += (file -> x))
      toc += (file -> getLine(header, "\"toc\"").isDefined)
    }
  }

}
