package models

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

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

case class Refresh()

case class Connect()

case class Connected(enumerator: Enumerator[JsValue])

object Statistics {

  implicit val timeout = Timeout(5 second)

  var actors: Map[String, ActorRef] = Map()

  def actor(id: String) = actors.synchronized {
    actors.find(_._1 == id).map(_._2) match {
      case Some(actor) => actor

      case None => {
        val actor = Akka.system.actorOf(Props(new StatisticsActor(id)), name = s"host-$id")
        Akka.system.scheduler.schedule(0.seconds, 3.second, actor, Refresh)
        actors += (id -> actor)
        actor
      }
    }
  }

  def attach(query: String): Future[(Iteratee[JsValue, _], Enumerator[JsValue])] = {
    (actor(query) ? Connect()).map {
      case Connected(enumerator) => (Iteratee.ignore[JsValue], enumerator)
    }
  }
}