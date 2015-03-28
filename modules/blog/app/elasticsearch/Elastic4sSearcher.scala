package elasticsearch

import com.sksamuel.elastic4s.ElasticDsl._
import scala.concurrent.Future
import play.api.libs.json.Json
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by bharadwaj on 14/04/14.
 */
trait Elastic4sSearcher extends Elastic4sIndexer {

  def searchCount: Long = {
    val resp = client.sync.execute {
      search in blogIndex -> postType
    }
    resp.getHits.getTotalHits
  }

  def searchText(queryString: String, count: Int, page: Int): Future[String] = {
    val searchFuture = client.execute {
      search in blogIndex -> postType query matchQuery(contentField, queryString) start page limit count highlighting(contentField)
    }

    val s = searchFuture.flatMap(search => {
      Future {
        val hits = search.getHits.getHits
        //search.getHits.
        var searches = scala.collection.mutable.Seq[Search]()
        for {h <- hits} {

          var fragments = h.highlightFields().get(contentField).fragments()
          var fs = scala.collection.mutable.Seq[String]()
          for(fragment <- fragments) {
            //Logger.info("fragment = " + fragment)
            fs = fs :+ fragment.string()
          }
          //val f = new Fragment(fs)

          val s = h.getSource
          val search = Search(s.get(titleField).toString, s.get(urlField).toString,
            s.get(tagsField).toString, s.get(categoryField).toString,
            s.get(dateField).toString, s.get(contentField).toString, h.getScore, fs)
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
