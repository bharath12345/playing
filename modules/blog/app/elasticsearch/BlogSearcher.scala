package elasticsearch

import scala.concurrent.Future
import com.sksamuel.elastic4s.ElasticDsl._
import play.api.libs.json.Json
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by bharadwaj on 10/04/14.
 */
object BlogSearcher extends BlogElasticSearch {

  def searchCount: Long = {
    val resp = client.sync.execute {
      search in blogIndex -> postType
    }
    resp.getHits.getTotalHits
  }

  def searchText(queryString: String, count: Int, page: Int): Future[String] = {
    val searchFuture = client.execute {
      search in blogIndex -> postType query queryString start page limit count
    }

    val s = searchFuture.flatMap(search => {
      Future {
        val hits = search.getHits.getHits
        var searches = scala.collection.mutable.Seq[Search]()
        for {h <- hits} {
          val s = h.getSource
          val search = Search(s.get(titleField).toString, s.get(subheadingField).toString,
            s.get(tagsField).toString, s.get(categoryField).toString,
            s.get(dateField).toString, s.get(descriptionField).toString, s.get(contentField).toString)
          searches = searches :+ search
        }
        val s: Searches = Searches(searches)
        val json = Json.toJson(s)
        Json.stringify(json)
      }
    })
    s
  }
}
