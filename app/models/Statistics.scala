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

  // There is one statistics actor for each period which broadcasts tweet-counts for all stubs (for that period) on the websocket
  var actors: Map[Int, ActorRef] = Map()

  def actor(period: Int) = actors.synchronized {
    actors.find(_._1 == period).map(_._2) match {
      case Some(actor) => {
        Logger.info(s"reusing existing actor for $period")
        actor
      }

      case None => {
        Logger.info(s"creating new actor for $period")
        val actor = Akka.system.actorOf(Props(new StatisticsActor()), name = s"period-$period")

        // setup a scheduler according to the actor's period
        period match {
          case 0 => Akka.system.scheduler.schedule(0.seconds, 3.seconds,  actor, Refresh3)
          case 1 => Akka.system.scheduler.schedule(0.seconds, 30.seconds, actor, Refresh30)
          case 2 => Akka.system.scheduler.schedule(0.seconds, 5.minutes,  actor, Refresh300)
          case 3 => Akka.system.scheduler.schedule(0.seconds, 30.minutes, actor, Refresh1800)
          case 4 => Akka.system.scheduler.schedule(0.seconds, 3.hours,    actor, Refresh10800)
        }

        actors += (period -> actor)
        actor
      }
    }
  }

  def attach(period: Int): Future[(Iteratee[JsValue, _], Enumerator[JsValue])] = {
    (actor(period) ? Connect()).map {
      case Connected(enumerator) => {
        (Iteratee.ignore[JsValue], enumerator)
      }
    }
  }
}