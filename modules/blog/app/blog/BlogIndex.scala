package blog

import scala.collection.immutable.HashMap
import org.joda.time.DateTime
import org.pegdown.PegDownProcessor
import play.api.{Application, Environment}

/**
 * Created by bharadwaj on 09/04/14.
 */
object BlogIndex extends Posts {

  var dateMap = new HashMap[String, String]
  var contentMap = new HashMap[String, String]
  var fileMap = new HashMap[String, String]
  var sortedTitleList = List[String]()
  var pathMap = new HashMap[String, String]

  def setupIndexPage(env: Environment) = {
    var titleMap = new HashMap[Long, String]
    for (file <- posts) {
      val lines = fileContent(env, "conf/posts/" + file)
      val header = lines.takeWhile(line => !line.equals("}}}")).toSeq
      val content = lines.dropWhile(line => !line.equals("}}}")).drop(1).dropWhile(line => line.length == 0)
      val excerpt = pegdown.markdownToHtml(content.takeWhile(line => line.length != 0)(0))
      //println(excerpt)

      val title = getLine(header, "\"title\"")
      //println("title = " + title + " for file = " + file)
      val date = getLine(header, "\"date\"")

      val ymd = date.split("-")
      val dt = new DateTime(ymd(2).toInt, ymd(0).toInt, ymd(1).toInt, 0, 0, 0)
      //println("date = " + dt)

      dateMap += (title -> date)
      titleMap += (dt.getMillis -> title)
      fileMap += (title -> file.replace(".md", ""))
      contentMap += (title -> excerpt)
      pathMap += (title -> ("post/" + file.replace(".md", "")))
    }

    sortedTitleList = titleMap.toList.sortWith(_._1 > _._1).map(_._2) // sort by date
    //println(sortedTitleList)
  }


}
