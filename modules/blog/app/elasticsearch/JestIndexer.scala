package elasticsearch

import io.searchbox.indices.CreateIndex
import scala.concurrent.{Await, Future}
import io.searchbox.client.{JestClient, JestClientFactory, JestResult}
import play.api.libs.json.{JsString, JsObject, JsValue}
import io.searchbox.core.Index
import play.api.Logger
import scala.concurrent.duration.{FiniteDuration, DurationInt}
import scala.concurrent.ExecutionContext.Implicits.global
import io.searchbox.indices.CreateIndex
import io.searchbox.core.Index
import play.api.Logger
import io.searchbox.client.config.HttpClientConfig
import scala.language.postfixOps


/**
 * Created by bharadwaj on 14/04/14.
 */
trait JestIndexer extends Indexer {

  def createIndex = {
    Logger.info("going to connect to bonsai host = " + bonsaiHost)
    val factory: JestClientFactory = new JestClientFactory()
    val httpClientConfig :HttpClientConfig = new HttpClientConfig.Builder("http://" + bonsaiHost + ":9200").multiThreaded(true).build()
    factory.setHttpClientConfig(httpClientConfig)
    val jestClient: JestClient = factory.getObject()


    jestClient.execute(new CreateIndex.Builder(blogIndex).build())

    val sf: Seq[Future[JestResult]] =
      for (s <- searches.s)
      yield (
        Future {
          val json: JsValue = JsObject(Seq(
            titleField -> JsString(getOrNull(s.title)),
            urlField -> JsString(getOrNull(s.url)),
            tagsField -> JsString(getOrNull(s.tags)),
            categoryField -> JsString(getOrNull(s.category)),
            dateField -> JsString(getOrNull(s.date)),
            contentField -> JsString(getOrNull(s.content))
          ))

          val jsonString = json.toString()
          val index: Index = new Index.Builder(jsonString).index(blogIndex).`type`(postType).build()
          jestClient.execute(index);

        })

    val f = Future.sequence(sf)
    Await.ready(f, 1 minute)
    Logger.info("Finished document insertions.")
  }

  def shutdown = {

  }

}
