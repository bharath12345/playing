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
import models.FlushOneHour
import models.FlushOneDay
import models.FlushOneMonth
import scala.Some
import models.FlushThreeHours
import models.FlushOneWeek
import tempo.WriterActor

case class Connect()

case class Connected(enumerator: Enumerator[JsValue])

object Statistics {

  implicit val timeout = Timeout(5 second)

  // There is one statistics actor for each period which broadcasts tweet-counts for all stubs (for that period) on the websocket
  val actors = scala.collection.mutable.Map[Refresh,ActorRef]()

  val postgresPersistor: ActorRef = Akka.system.actorOf(Props(new PersistorActor()))
  val tempoPersistor: ActorRef = Akka.system.actorOf(Props(new WriterActor()))

  createScheduledMsgs(postgresPersistor)
  createScheduledMsgs(tempoPersistor)

  def createScheduledMsgs(aref: ActorRef) = {
    Akka.system.scheduler.schedule(0.seconds, FlushOneHour().checkDuration,    aref, FlushOneHour())
    Akka.system.scheduler.schedule(0.seconds, FlushThreeHours().checkDuration, aref, FlushThreeHours())
    Akka.system.scheduler.schedule(0.seconds, FlushOneDay().checkDuration,     aref, FlushOneDay())
    Akka.system.scheduler.schedule(0.seconds, FlushOneWeek().checkDuration,    aref, FlushOneWeek())
    Akka.system.scheduler.schedule(0.seconds, FlushOneMonth().checkDuration,   aref, FlushOneMonth())
  }

  def actor(refresh: Refresh) = actors.synchronized {
    actors.find(_._1 == refresh).map(_._2) match {
      case Some(actor) => {
        Logger.info(s"reusing existing actor for $refresh")
        actor
      }

      case None => {
        Logger.info(s"creating new actor for $refresh")
        val actorName = "statisticsActorPeriod" + refresh.period
        val actor = Akka.system.actorOf(Props(new StatisticsActor(postgresPersistor, tempoPersistor)), name = actorName)

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