package elasticsearch

import blog.Posts
import com.sksamuel.elastic4s.ElasticClient
import play.api.libs.json.{Json, Writes}
import scala.concurrent.ExecutionContext.Implicits.global
import play.Logger
import io.searchbox.client.{JestClient, JestClientFactory}
import io.searchbox.client.config.HttpClientConfig
import scala.collection.immutable.HashSet

/**
 * Created by bharadwaj on 10/04/14.
 */
trait Indexer extends Posts {

  var searches: Searches = setupIndex

  case class Search(title: String, url: String, tags: String, category: String,
                    date: String, content: String, score: Float, fragments: Seq[String])

  case class Searches(s: Seq[Search])

  implicit val searchWrites = new Writes[Search] {
    def writes(s: Search) = Json.obj(
      titleField       -> s.title,
      urlField         -> s.url,
      tagsField        -> s.tags,
      categoryField    -> s.category,
      dateField        -> s.date,
      contentField     -> s.content,
      scoreField       -> s.score,
      fragmentsField   -> s.fragments
    )
  }

  implicit val searchesWrites = new Writes[Searches] {
    def writes(searches: Searches) = Json.obj(
      searchField -> searches.s
    )
  }

  def getOrNull(s: String): String = if (s.length > 0) s else "none"

  def setupIndex: Searches = {
    var searches = scala.collection.mutable.Seq[Search]()
    for (file <- posts) {
      val lines = fileContent("public/posts/" + file)
      val header = lines.takeWhile(line => !line.equals("}}}")).toSeq
      val title = getLine(header, "\"title\"")
      val date = getLine(header, "\"date\"")

      //val subheading = getLine(header, "\"subheading\"")
      val category = getLine(header, "\"category\"")
      //val description = getLine(header, "\"description\"")
      val tagLine = getLine(header, "\"tags\"").filter(!"[]\"".contains(_))

      val tags = tagLine.split(" ")
      var tagSet = new HashSet[String]
      for {tag <- tags; if (tag.length > 0)} {
        //println("tag = " + tag)
        tagSet += tag.trim
      }

      val content: String = lines.dropWhile(line => !line.equals("}}}")).drop(1).mkString(" ")
      val search = Search(title, "post/" + file.replaceAll(".md", ""), tagLine, category, date, content, 0, Seq())
      searches = searches :+ search
    }
    Searches(searches)
  }

  def createIndex

  def shutdown

}
