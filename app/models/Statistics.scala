package models

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
import models.{StatisticsActor}
import play.api._

case class Refresh3()
case class Refresh30()
case class Refresh300()
case class Refresh1800()
case class Refresh10800()

case class Connect()

case class Connected(enumerator: Enumerator[JsValue])

object Statistics {

  implicit val timeout = Timeout(5 second)

  var actors: Map[String, ActorRef] = Map()

  def actor(query: String, period: FiniteDuration) = actors.synchronized {
    actors.find(_._1 == query).map(_._2) match {
      case Some(actor) => {
        Logger.info(s"reusing existing actor for $query")
        actor
      }

      case None => {
        Logger.info(s"creating new actor for $query")
        val actor = Akka.system.actorOf(Props(new StatisticsActor("india")), name = s"host-$query")
        Akka.system.scheduler.schedule(0.seconds, 3.seconds,  actor, Refresh3)
        Akka.system.scheduler.schedule(0.seconds, 30.seconds, actor, Refresh30)
        Akka.system.scheduler.schedule(0.seconds, 5.minutes,  actor, Refresh300)
        Akka.system.scheduler.schedule(0.seconds, 30.minutes, actor, Refresh1800)
        Akka.system.scheduler.schedule(0.seconds, 3.hours,    actor, Refresh10800)
        actors += (query -> actor)
        actor
      }
    }
  }

  def attach(query: String, period: FiniteDuration): Future[(Iteratee[JsValue, _], Enumerator[JsValue])] = {
    (actor(query, period) ? Connect()).map {
      case Connected(enumerator) => {
        Logger.info(s"received connected response for actor $query")
        (Iteratee.ignore[JsValue], enumerator)
      }
    }
  }
}