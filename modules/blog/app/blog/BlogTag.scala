package blog

import scala.collection.immutable.{HashMap, HashSet}

/**
 * Created by bharadwaj on 09/04/14.
 */
object BlogTag extends Posts {

  var tagSet = new HashSet[String]
  var titleMap = new HashMap[String, String]
  var dateMap = new HashMap[String, String]
  var tagLineMap = new HashMap[String, String]

  def setupTags = {
    for (file <- posts) {
      val lines = fileContent("public/posts/" + file)
      val header = lines.takeWhile(line => !line.equals("}}}")).toSeq

      val tagLine = getLine(header, "\"tags\"").filter(!"[]\"".contains(_))
      tagLineMap += (tagLine -> file)

      val tags = tagLine.split(" ")
      //println("tags = " + tags.mkString(","))

      for {tag <- tags; if (tag.length > 0)} {
        //println("tag = " + tag)
        tagSet += tag.trim
      }

      dateMap  += (file -> getLine(header, "\"date\""))
      titleMap += (file -> getLine(header, "\"title\""))
    }
  }
}
