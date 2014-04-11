package elasticsearch

import blog.Posts
import com.sksamuel.elastic4s.ElasticClient
import play.api.libs.json.{Json, Writes}
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by bharadwaj on 10/04/14.
 */
trait BlogElasticSearch extends Posts {

  val client = ElasticClient.local

  case class Search(title: String, url: String, subheading: String, tags: String, category: String,
                    date: String, description: String, content: String, score: Float, fragments: Seq[String])

  case class Searches(s: Seq[Search])

  implicit val searchWrites = new Writes[Search] {
    def writes(s: Search) = Json.obj(
      titleField       -> s.title,
      subheadingField  -> s.subheading,
      tagsField        -> s.tags,
      categoryField    -> s.category,
      dateField        -> s.date,
      descriptionField -> s.description,
      contentField     -> s.content,
      scoreField       -> s.score,
      "fragments"       -> s.fragments
    )
  }

  implicit val searchesWrites = new Writes[Searches] {
    def writes(searches: Searches) = Json.obj(
      "search" -> searches.s
    )
  }
}
