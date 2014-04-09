package elasticsearch

import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._
import blog.Posts
import scala.collection.immutable.HashSet
import play.api.Logger

/**
 * Created by bharadwaj on 09/04/14.
 */
object BlogIndexer extends Posts {

  val client = ElasticClient.local   //.remote("localhost", 9300)

  val blogIndex: String = "blog"
  val postType: String = "post"

  def setupSearch = {
    createIndex
    for (file <- posts) {
      val lines  = fileContent("public/posts/" + file)
      val header = lines.takeWhile(line => !line.equals("}}}"))
      val title  = getLine(header, "\"title\"")
      val date   = getLine(header, "\"date\"")

      val subheading  = getLine(header, "\"subheading\"")
      val category    = getLine(header, "\"category\"")
      val description = getLine(header, "\"description\"")
      val tagLine     = getLine(header, "\"tags\"").filter(!"[]\"".contains(_))

      val tags = tagLine.split(" ")
      var tagSet = new HashSet[String]
      for {tag <- tags; if (tag.length > 0)} {
        //println("tag = " + tag)
        tagSet += tag.trim
      }

      val content: String = lines.dropWhile(line => !line.equals("}}}")).drop(1).mkString(" ")
      insertBlog(title, subheading, tags, category, date, description, content)
    }
  }

  def createIndex = {
    Logger.info("Creating Elastic Search Index = " + blogIndex)
    client.sync.execute { deleteIndex(blogIndex) }
    client.sync.execute { create.index(blogIndex) }
  }

  def insertBlog(title: String, subheading: String, tags: Array[String], category: String,
                  date: String, description: String, content: String) = {
    Logger.info("Adding document to Elastic Search. Title = " + title)
    client.sync.execute {
      index into blogIndex + "/" + postType fields (
        "title"       -> title,
        "subheading"  -> "sub",
        "tags"        -> "tags",
        "category"    -> "cat",
        "date"        -> date,
        "description" -> "des",
        "content"     -> content
        )
    }
  }

  def searchOne: Long = {
    val resp = client.sync.execute { search in blogIndex -> postType }
    resp.getHits.getTotalHits
  }

  def searchText(q: String): String = {
    val resp = client.sync.execute { search in blogIndex -> postType query q}
    resp.getHits.getAt(0).getSourceAsString
  }

}
