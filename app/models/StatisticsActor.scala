package models

import java.util.Date

import akka.actor.Actor

import play.api.libs.iteratee.{Enumerator, Concurrent}
import play.api._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json._

import twitter.Cache

class StatisticsActor() extends Actor {

  var channel: Concurrent.Channel[JsValue] = _

  def receive = {
    case Connect() => {
      Logger.info(s"received connect message.")
      val enumerator = Concurrent.unicast[JsValue] {
        c => channel = c
      }
      sender ! Connected(enumerator)
    }

    case Refresh3 => {
      Logger.info(s"received 3sec refresh message.")
      broadcastCount(Cache.getTweetCount(0), 0)
      Cache.flush(0)
    }

    case Refresh30 => {
      Logger.info(s"received 30sec refresh message.")
      broadcastCount(Cache.getTweetCount(1), 1)
      Cache.flush(1)
    }

    case Refresh300 => {
      Logger.info(s"received 300sec refresh message.")
      broadcastCount(Cache.getTweetCount(2), 2)
      Cache.flush(2)
    }

    case Refresh1800 => {
      Logger.info(s"received 1800sec refresh message.")
      broadcastCount(Cache.getTweetCount(3), 3)
      Cache.flush(3)
    }

    case Refresh10800 => {
      Logger.info(s"received 10800sec refresh message.")
      broadcastCount(Cache.getTweetCount(4), 4)
      Cache.flush(4)
    }
  }

  def broadcastCount(counter: Map[String, Long], period: Int) = {
    val date = new Date().getTime()

    val ja = Json.arr()
    counter foreach {
      case (key, value) => {
        val jo = Json.obj(
          "query"     -> JsString(key),
          "tweets"    -> JsNumber(value)
        )
        ja.append(jo)
      }
    }

    val msg = Json.obj(
      "timestamp" -> JsNumber(date),
      "period"    -> JsNumber(period),
      "values"    -> ja
      )

    Logger.info("sending json message = " + msg.toString())
    channel.push(msg)
  }

}