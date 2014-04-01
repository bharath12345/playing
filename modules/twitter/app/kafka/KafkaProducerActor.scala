package kafka

import kafka.javaapi.producer.Producer
import kafka.producer.KeyedMessage
import akka.actor.Actor
import _root_.twitter.Configuration
import models.twitter._
import models._
import play.api.libs.json.{Json, JsValue}
import play.api.Logger
import org.joda.time.DateTime
import models.FlushOneHour
import models.Refresh30
import models.FlushOneDay
import models.Refresh1800
import models.twitter.PersistenceMsg
import models.Refresh3
import models.Refresh10800
import models.Refresh300
import models.FlushThreeHours
import models.FlushOneWeek
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by bharadwaj on 30/03/14.
 */
class KafkaProducerActor() extends Actor with Configuration {

  private val producerCreated = new AtomicBoolean(false)

  var producer: KafkaProducer = null

  def init(topic: String, zklist: String) = {
    if (producerCreated.getAndSet(true)) {
      throw new RuntimeException(this.getClass.getSimpleName + " this kafka akka actor has a producer already")
    }

    producer = new KafkaProducer(topic,zklist)
  }


  override def receive: Actor.Receive = {

    case t: (String, String) ⇒ {
      init(t._1, t._2)
    }

    case PersistenceMsg(Refresh3(), j: JsValue) => {
      Logger.info(s"received Refresh 3 seconds message.")

      //val topic:String = "mytopic";

      val messageStr: String = Json.stringify(j)

      //val data: KeyedMessage[Integer, String] = new KeyedMessage[Integer, String](topic, messageStr)
      producer.send(messageStr)
    }

    case PersistenceMsg(Refresh30(), j: JsValue) => {
      Logger.info(s"received Refresh 30 seconds message.")
    }

    case PersistenceMsg(Refresh300(), j: JsValue) => {
      Logger.info(s"received Refresh 300 seconds message.")
    }

    case PersistenceMsg(Refresh1800(), j: JsValue) => {
      Logger.info(s"received Refresh 1800 seconds message.")
    }

    case PersistenceMsg(Refresh10800(), j: JsValue) => {
      Logger.info(s"received Refresh 10800 seconds message.")
    }

    case f: FlushOneHour => {
    }

    case FlushThreeHours() => {

    }

    case FlushOneDay() => {

    }

    case FlushOneWeek() => {

    }

    case FlushOneMonth() => {

    }
  }
}
