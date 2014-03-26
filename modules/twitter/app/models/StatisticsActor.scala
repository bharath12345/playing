package models.twitter

import java.util.Date

import akka.actor.{ActorRef, Props, Actor}

import play.api.libs.iteratee.{Enumerator, Concurrent}
import play.api._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json._
import play.api.Play.current
import play.api.libs.concurrent.Akka

import _root_.twitter.Cache
import models._
import scala.concurrent.duration.{FiniteDuration, DurationInt}
import play.api.libs.json.JsString
import scala.Some
import play.api.libs.json.JsNumber

sealed case class PersistenceMsg(r: Refresh, j: JsValue)

class StatisticsActor(persistor: ActorRef) extends Actor {

  val (enumerator, channel) = Concurrent.broadcast[JsValue]

  def receive = {
    case Connect() => {
      Logger.info(s"received connect message.")
      sender ! Connected(enumerator)
    }

    case r: Refresh => {
      Logger.info(s"received refresh message of duration = $r.duration")
      val counter = Cache.getTweetCount(r)
      counter match {
        case Some(twCounter) => {
          val j = broadcastCount(twCounter, r)
          Cache.flush(r)
          persistor ! PersistenceMsg(r, j)
        }
        case None => {
          Logger.info("Not sending the null json... not connected to twitter feed yet")
        }
      }
    }
  }

  def broadcastCount(counter: Map[String, Long], r: Refresh): JsValue = {
    val date = new Date().getTime()

    var ja = Json.arr()
    counter foreach {
      case (key, value) => {
        //Logger.info(s"stub = $key tweets = $value")
        val jo = Json.obj(
          "stub"     -> JsString(key),
          "tweets"    -> JsNumber(value)
        )
        ja = ja :+ jo
        //Logger.info("ja = " + ja.toString())
        //Logger.info("jo = " + jo.toString())
      }
    }

    val msg = Json.obj(
      "timestamp" -> JsNumber(date),
      "period"    -> JsNumber(r.period),
      "values"    -> ja
      )

    Logger.info("sending json message = " + msg.toString())
    channel.push(msg)
    msg
  }

}