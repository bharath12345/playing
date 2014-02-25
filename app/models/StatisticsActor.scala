package models

import java.util.Date

import scala.util.Random

import akka.actor.Actor
import play.api.libs.iteratee.Concurrent
import play.api.libs.json.JsNumber
import play.api.libs.json.JsObject
import play.api.libs.json.JsString
import play.api.libs.json.JsValue
import twitter.Cache
import play.api._

class StatisticsActor(query: String) extends Actor {

  val (enumerator, channel) = Concurrent.broadcast[JsValue]

  def receive = {
    case Connect() => {
      Logger.info(s"received connect message. actor = $query")
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

  def broadcast(timestamp: Long) {
    val msg = JsObject(
      Seq(
        "query" -> JsString(query),
        "counter" -> JsObject(
          Seq(
            ("timestamp" -> JsNumber(timestamp)),
            ("random" -> JsNumber(Random.nextInt(100)))
          )
        )
      )
    )
    channel.push(msg)
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