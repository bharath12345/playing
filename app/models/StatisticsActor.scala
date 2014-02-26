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

    case Refresh3 => {
      Logger.info(s"received refresh message. actor = $query")
      broadcastTweetCount(new Date().getTime())
      Cache.flush3(query)
    }

    case Refresh30 => {
      Logger.info(s"received refresh message. actor = $query")
      broadcastTweetCount(new Date().getTime())
      Cache.flush30(query)
    }

    case Refresh300 => {
      Logger.info(s"received refresh message. actor = $query")
      broadcastTweetCount(new Date().getTime())
      Cache.flush300(query)
    }

    case Refresh1800 => {
      Logger.info(s"received refresh message. actor = $query")
      broadcastTweetCount(new Date().getTime())
      Cache.flush1800(query)
    }

    case Refresh10800 => {
      Logger.info(s"received refresh message. actor = $query")
      broadcastTweetCount(new Date().getTime())
      Cache.flush10800(query)
    }
  }

  def broadcastTweetCount(timestamp: Long) {
    val counter = Cache.getP3TweetCount(query) match {
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