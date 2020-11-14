package blog

import play.api.{Environment, Logging}

import scala.collection.immutable.{HashMap, HashSet}

/**
 * Created by bharadwaj on 09/04/14.
 */
object BlogCategory extends Posts with Logging {

  var categorySet = new HashSet[String]
  var titleMap = new HashMap[String, String]
  var categoryMap = new HashMap[String, String]

  def setupCategories(env: Environment) = {
    for (file <- posts) {
      val lines = fileContent(env, file)
      val header = lines.takeWhile(line => !line.equals("}}}")).toSeq
      getLine(header, "\"category\"").filter(!"\"".contains(_)).foreach { category =>
        logger.info("category = " + category)
        categorySet += category
        categoryMap += (category -> file)

        getLine(header, "\"title\"").foreach(x => titleMap += (file -> x))
      }
    }
  }

}
