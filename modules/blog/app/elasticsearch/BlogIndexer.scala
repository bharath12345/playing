package elasticsearch

import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._
import blog.Posts
import scala.collection.immutable.HashSet
import play.api.Logger
import play.api.libs.json.{Writes, Json}
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse
import scala.async.Async.{async, await}
import scala.concurrent.{Await, Future, ExecutionContext}
import scala.concurrent.duration.{FiniteDuration, DurationInt}
import scala.concurrent.Future
import org.elasticsearch.action.index.IndexResponse

/**
 * Created by bharadwaj on 09/04/14.
 */
object BlogIndexer extends Posts {

  val client = ElasticClient.local //.remote("localhost", 9300)

  val blogIndex: String = "blog"
  val postType: String = "post"

  val titleField: String = "title"
  val subheadingField: String = "subheading"
  val tagsField: String = "tags"
  val categoryField: String = "category"
  val dateField: String = "date"
  val descriptionField: String = "description"
  val contentField: String = "content"

  val searches = setupIndex


  case class Search(title: String, subheading: String, tags: String, category: String,
                    date: String, description: String, content: String)

  case class Searches(s: Seq[Search])

  implicit val searchWrites = new Writes[Search] {
    def writes(s: Search) = Json.obj(
      titleField -> s.title,
      subheadingField -> s.subheading,
      tagsField -> s.tags,
      categoryField -> s.category,
      dateField -> s.date,
      descriptionField -> s.description,
      contentField -> s.content
    )
  }

  implicit val searchesWrites = new Writes[Searches] {
    def writes(searches: Searches) = Json.obj(
      "search" -> searches.s
    )
  }


  def setupIndex: Searches = {
    var searches = scala.collection.mutable.Seq[Search]()
    for (file <- posts) {
      val lines = fileContent("public/posts/" + file)
      val header = lines.takeWhile(line => !line.equals("}}}"))
      val title = getLine(header, "\"title\"")
      val date = getLine(header, "\"date\"")

      val subheading = getLine(header, "\"subheading\"")
      val category = getLine(header, "\"category\"")
      val description = getLine(header, "\"description\"")
      val tagLine = getLine(header, "\"tags\"").filter(!"[]\"".contains(_))

      val tags = tagLine.split(" ")
      var tagSet = new HashSet[String]
      for {tag <- tags; if (tag.length > 0)} {
        //println("tag = " + tag)
        tagSet += tag.trim
      }

      val content: String = lines.dropWhile(line => !line.equals("}}}")).drop(1).mkString(" ")
      val search = Search(title, subheading, "t", category, date, description, content)
      searches = searches :+ search
    }
    Searches(searches)
  }

  /*def createIndex = {
    Logger.info("Creating Elastic Search Index = " + blogIndex)
    val deleteFuture = client.execute {
      deleteIndex(blogIndex)
    }

    deleteFuture.flatMap(d => {
      val createFuture = client.execute {
        create.index(blogIndex)
      }

      createFuture.flatMap(c => {
        val searches = setupIndex
        val x = for { s <- searches.s}
          yield insertBlog(s)

      })

      createFuture
    })
  }*/

  val dIndex =  {
    Logger.info("Starting Index Delete...")
    client.execute { deleteIndex(blogIndex) }
  }
  val cIndex = {
    Logger.info("Starting Index Create...")
    client.execute { create.index(blogIndex) }
  }

  def createIndex = {
    Await.ready(dIndex, 1 minute)
    Logger.info("Finished Index Delete.")

    Await.ready(cIndex, 1 minute)
    Logger.info("Finished Index Creation.")

    // convert the sequence of futures to => future of a sequence, that is
    // Seq[Future[IndexResponse]] => Future[Seq[IndexResponse]]
    // And finally wait on that single future to complete
    val f = Future.sequence(insertBlog.toList)
    Await.ready(f, 1 minute)
    Logger.info("Finished document insertions.")
  }

  implicit class ForeachAsync[T](iterable: Iterable[T]) {
    def foreachAsync[U](f: T => U)(implicit ec: ExecutionContext): Unit = {
      def next(i: Iterator[T]): Unit = if (i.hasNext) Future(f(i.next)) onComplete { case _ => next(i) }
      next(iterable.iterator)
    }
  }

  val insertBlog: Seq[Future[IndexResponse]] =
    for (s <- searches.s)
      yield client.execute {
        index into blogIndex + "/" + postType fields(
          titleField -> s.title,
          subheadingField -> "sub",
          tagsField -> "tags",
          categoryField -> "cat",
          dateField -> s.date,
          descriptionField -> "des",
          contentField -> s.content
          )
      }

  def searchOne: Long = {
    val resp = client.sync.execute {
      search in blogIndex -> postType
    }
    resp.getHits.getTotalHits
  }


  def searchText(q: String): String = {
    val resp = client.sync.execute {
      search in blogIndex -> postType query q
    }
    val hits = resp.getHits.getHits
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

}
