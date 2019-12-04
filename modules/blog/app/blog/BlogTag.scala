package blog

import play.api.{Environment, Logging}

import scala.collection.immutable.{HashMap, HashSet}

/**
 * Created by bharadwaj on 09/04/14.
 */
object BlogTag extends Posts with Logging {

  var tagSet = new HashSet[String]
  var titleMap = new HashMap[String, String]
  var dateMap = new HashMap[String, String]
  var tagLineMap = new HashMap[String, String]

  def setupTags(env: Environment) = {
    for (file <- posts) {
      val lines = fileContent(env, file)
      val header = lines.takeWhile(line => !line.equals("}}}")).toSeq

      getLine(header, "\"tags\"").filter(!"[]\"".contains(_)).foreach { tagLine =>
        tagLineMap += (tagLine -> file)

        val tags = tagLine.split(" ")
        logger.info("tags = " + tags.mkString(","))

        for {tag <- tags; if (tag.length > 0)} {
          logger.info("tag = " + tag)
          tagSet += tag.trim
        }

        getLine(header, "\"date\"").foreach(x => dateMap += (file -> x))
        getLine(header, "\"title\"").foreach(x => titleMap += (file -> x))
      }
    }
  }
}
