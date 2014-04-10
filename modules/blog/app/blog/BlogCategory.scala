package blog

import scala.collection.immutable.{HashMap, HashSet}

/**
 * Created by bharadwaj on 09/04/14.
 */
object BlogCategory extends Posts {

  var categorySet = new HashSet[String]
  var titleMap = new HashMap[String, String]
  var dateMap = new HashMap[String, String]
  var categoryMap = new HashMap[String, String]

  def setupCategories = {
    for (file <- posts) {
      val lines = fileContent("public/posts/" + file)
      val header = lines.takeWhile(line => !line.equals("}}}")).toSeq
      val category = getLine(header, "\"category\"").filter(!"\"".contains(_)).trim
      //println("category = " + category)
      categorySet += category

      categoryMap += (category -> file)
      dateMap     += (file -> getLine(header, "\"date\""))
      titleMap    += (file -> getLine(header, "\"title\""))
    }
  }

}
