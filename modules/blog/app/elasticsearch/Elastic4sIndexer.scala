package elasticsearch

import com.sksamuel.elastic4s.ElasticClient
import play.api.Logger
import com.sksamuel.elastic4s.ElasticDsl._
import scala.concurrent.{Future, Await}
import scala.concurrent.duration.{FiniteDuration, DurationInt}
import org.elasticsearch.action.index.IndexResponse
import scala.concurrent.ExecutionContext.Implicits.global
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse
import org.elasticsearch.action.index.IndexResponse
import com.sksamuel.elastic4s.ElasticClient
import scala.language.postfixOps

/**
 * Created by bharadwaj on 14/04/14.
 */
/*trait Elastic4sIndexer extends Indexer {

  val client = ElasticClient.local

  val dIndex = {
    Logger.info("Starting Index Delete...")
    client.execute {
      deleteIndex(blogIndex)
    }
  }
  val cIndex = {
    Logger.info("Starting Index Create...")
    client.execute {
      create.index(blogIndex)
    }
  }

  def createIndex = {

    Await.ready(dIndex, 1 minute)
    Logger.info("Finished Index Delete.")

    Await.ready(cIndex, 1 minute)
    Logger.info("Finished Index Creation.")

    // convert the sequence of futures to => future of a sequence, that is
    // Seq[Future[IndexResponse]] => Future[Seq[IndexResponse]]
    // And finally wait on that single future to complete
    val f = Future.sequence(insertBlog)
    Await.ready(f, 1 minute)
    Logger.info("Finished document insertions.")
  }


  val insertBlog: Seq[Future[IndexResponse]] =
    for (s <- searches.s)
    yield client.execute {
      index into blogIndex + "/" + postType fields(
        titleField -> getOrNull(s.title),
        urlField -> getOrNull(s.url),
        tagsField -> getOrNull(s.tags),
        categoryField -> getOrNull(s.category),
        dateField -> getOrNull(s.date),
        contentField -> getOrNull(s.content)
        )
    }

  def shutdown = client.close()

} */
