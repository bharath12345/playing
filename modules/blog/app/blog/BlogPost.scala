package blog

import scala.collection.immutable.HashMap

/**
 * Created by bharadwaj on 09/04/14.
 */
object BlogPost extends Posts {

  var title = new HashMap[String, String]
  var content = new HashMap[String, String]
  var date = new HashMap[String, String]
  var subheading = new HashMap[String, String]
  var toc = new HashMap[String, Boolean]

  def setupPosts = {
    for (file <- posts) {

      val lines = fileContent("public/posts/" + file)

      val header: Seq[String] = lines.takeWhile(line => !line.equals("}}}")).toSeq

      content += (file -> pegdown.markdownToHtml(lines.dropWhile(line => !line.equals("}}}")).drop(1).mkString("\n")))
      //println(content)

      title += (file -> getLine(header, "\"title\""))
      date += (file -> getLine(header, "\"date\""))
      subheading += (file -> getLine(header, "\"subheading\""))
      toc += (file -> getLine(header, "\"toc\"").toBoolean)
    }
  }

}
