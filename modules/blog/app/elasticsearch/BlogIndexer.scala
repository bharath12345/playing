package elasticsearch

import scala.collection.immutable.HashSet
import play.api.Logger
import scala.concurrent.ExecutionContext.Implicits.global
import scala.async.Async.{async, await}
import scala.concurrent.{Await, Future, ExecutionContext}
import scala.concurrent.duration.{FiniteDuration, DurationInt}
import scala.language.postfixOps

/**
 * Created by bharadwaj on 09/04/14.
 */
object BlogIndexer extends Indexer with JestIndexer {


}
