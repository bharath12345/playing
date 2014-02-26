package models

import java.util.Date

import scala.util.Random

import akka.actor.Actor
import play.api.libs.iteratee.{Enumerator, Concurrent}
import play.api.libs.json.JsNumber
import play.api.libs.json.JsObject
import play.api.libs.json.JsString
import play.api.libs.json.JsValue
import twitter.Cache
import play.api._
import play.api.libs.concurrent.Execution.Implicits._

class StatisticsActor(query: String) extends Actor {

  var channel: Concurrent.Channel[JsValue] = _

  def receive = {
    case Connect() => {
      Logger.info(s"received connect message. actor = $query")
      val enumerator = Concurrent.unicast[JsValue] {
        c => channel = c
      }
      sender ! Connected(enumerator)
    }

    case Refresh => {
      Logger.info(s"received refresh message. actor = $query")

      val time = new Date().getTime()
      //broadcast(time)
      broadcastTweetCount(time)

      Cache.flush(query)
    }
  }

  def broadcastTweetCount(timestamp: Long) {
    val counter = Cache.getTweetCount(query) match {
      case Some(count) => count
      case None => 0
    }

    val msg = JsObject(
      Seq(
        "query" -> JsString(query),
        "counter" -> JsObject(
          Seq(
            ("timestamp" -> JsNumber(timestamp)),
            ("tweets" -> JsNumber(counter))
          )
        )
      )
    )
    Logger.info("sending json message = " + msg.toString())
    channel.push(msg)
  }
}