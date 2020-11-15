package blog

import play.api.{Environment, Logging}

import scala.collection.immutable.{HashMap, HashSet}

/**
 * Created by bharadwaj on 09/04/14.
 */
object BlogTag extends Posts with Logging {

  def setupTags(env: Environment): Set[String] = {
    def convertTagLine(t: String): Array[String] =
      for {
        tt <- t.split(" ")
        if tt.length > 0 && tt != "]" && tt != "["
      } yield tt.trim.toLowerCase

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

}
