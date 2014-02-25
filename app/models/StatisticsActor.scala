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

class StatisticsActor(query: String) extends Actor {

  val (enumerator, channel) = Concurrent.broadcast[JsValue]

  def receive = {
    case Connect() => sender ! Connected(enumerator)
    case Refresh => {
      //broadcast(new Date().getTime(), hostid)
      broadcastTweetCount(new Date().getTime(), query)
      Cache.flush
    }
  }

  def broadcast(timestamp: Long, id: String) {
    val msg = JsObject(
      Seq(
        "id" -> JsString(id),
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

  def broadcastTweetCount(timestamp: Long, id: String) {
    val msg = JsObject(
      Seq(
        "id" -> JsString(id),
        "counter" -> JsObject(
          Seq(
            ("timestamp" -> JsNumber(timestamp)),
            ("tweets" -> JsNumber(Cache.tweetCounter))
          )
        )
      )
    )
    channel.push(msg)
  }
}