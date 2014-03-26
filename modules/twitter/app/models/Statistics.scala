package models.twitter

import scala.concurrent.Future
import scala.concurrent.duration.{FiniteDuration, DurationInt}

import akka.actor.{Props, ActorRef}
import akka.pattern.ask
import akka.util.Timeout
import play.api.Play.current
import play.api.libs.concurrent.Akka
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.iteratee.Enumerator
import play.api.libs.iteratee.Iteratee
import play.api.libs.json.JsValue
import play.api._
import models._
import models.twitter.Connected
import models.twitter.Connect
import scala.Some
import models.twitter.Connected
import models.twitter.Connect
import scala.Some
import models.FlushOneHour
import models.FlushOneDay
import models.FlushOneMonth
import models.twitter.Connected
import models.twitter.Connect
import scala.Some
import models.FlushThreeHours
import models.FlushOneWeek

case class Connect()

case class Connected(enumerator: Enumerator[JsValue])

object Statistics {

  implicit val timeout = Timeout(5 second)

  // There is one statistics actor for each period which broadcasts tweet-counts for all stubs (for that period) on the websocket
  val actors = scala.collection.mutable.Map[Refresh,ActorRef]()

  val persistor = Akka.system.actorOf(Props(new PersistorActor()))
  Akka.system.scheduler.schedule(0.seconds, FlushOneHour().duration, persistor, FlushOneHour())
  Akka.system.scheduler.schedule(0.seconds, FlushThreeHours().duration, persistor, FlushThreeHours())
  Akka.system.scheduler.schedule(0.seconds, FlushOneDay().duration, persistor, FlushOneDay())
  Akka.system.scheduler.schedule(0.seconds, FlushOneWeek().duration, persistor, FlushOneWeek())
  Akka.system.scheduler.schedule(0.seconds, FlushOneMonth().duration, persistor, FlushOneMonth())


  def actor(refresh: Refresh) = actors.synchronized {
    actors.find(_._1 == refresh).map(_._2) match {
      case Some(actor) => {
        Logger.info(s"reusing existing actor for $refresh")
        actor
      }

      case None => {
        Logger.info(s"creating new actor for $refresh")
        val actorName = "statisticsActorPeriod" + refresh.period
        val actor = Akka.system.actorOf(Props(new StatisticsActor(persistor.actorRef)), name = actorName)

        // setup a scheduler according to the actor's period
        Akka.system.scheduler.schedule(0.seconds, refresh.duration, actor, refresh)
        actors += (refresh -> actor)
        actor
      }
    }
  }

  def attach(refresh: Refresh): Future[(Iteratee[JsValue, _], Enumerator[JsValue])] = {
    (actor(refresh) ? Connect()).map {
      case Connected(enumerator) => {
        (Iteratee.ignore[JsValue], enumerator)
      }
    }
  }
}