package models.twitter

import java.util.Date

import akka.actor.Actor

import play.api.libs.iteratee.{Enumerator, Concurrent}
import play.api._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json._

import _root_.twitter.Cache
import models.Refresh
import play.api.libs.json.JsNumber
import play.api.libs.json.JsString

class StatisticsActor() extends Actor {

  val (enumerator, channel) = Concurrent.broadcast[JsValue]

  def receive = {
    case Connect() => {
      Logger.info(s"received connect message.")
      sender ! Connected(enumerator)
    }

    case r: Refresh => {
      Logger.info(s"received refresh message of duration = $r.duration")
      broadcastCount(Cache.getTweetCount(r), r)
      Cache.flush(r)
    }
  }

  def broadcastCount(counter: Map[String, Long], r: Refresh) = {
    val date = new Date().getTime()

    var ja = Json.arr()
    counter foreach {
      case (key, value) => {
        Logger.info(s"stub = $key tweets = $value")
        val jo = Json.obj(
          "stub"     -> JsString(key),
          "tweets"    -> JsNumber(value)
        )
        ja = ja :+ jo
        Logger.info("ja = " + ja.toString())
        Logger.info("jo = " + jo.toString())
      }
    }

    val msg = Json.obj(
      "timestamp" -> JsNumber(date),
      "period"    -> JsNumber(r.period),
      "values"    -> ja
      )

    Logger.info("sending json message = " + msg.toString())
    channel.push(msg)
  }

}