package kafka

import akka.actor.Actor
import _root_.twitter.Configuration
import play.api.libs.json.{Writes, JsResult, Json, JsValue}
import play.api.Logger
import scala.slick.jdbc.JdbcBackend._
import models.Refresh30
import models.Refresh1800
import models.twitter.ThreeSec
import models.twitter.PersistenceMsg
import models.Refresh3
import models.Refresh10800
import models.Refresh300
import models.TweetJson

/**
 * Created by bharadwaj on 30/03/14.
 */

case class KafkaConnect(topic: String, brokerlist: String)

class KafkaProducerActor() extends Actor with Configuration with TweetJson {

  val producerMap = scala.collection.mutable.Map[String, KafkaProducer]()

  case class Tweet(time: Long, stub: String, counter: Long)

  implicit val tweetWrites = new Writes[Tweet] {
    def writes(tweet: Tweet) = Json.obj(
      "time"    -> tweet.time,
      "stub"    -> tweet.stub,
      "counter" -> tweet.counter
    )
  }

  def persist(j: JsValue, producer: KafkaProducer) = {
    val tss: Seq[ThreeSec] = db.withSession {
      implicit session: Session =>
        val tweetJson: JsResult[TweetJson] = validate(j)
        retrievePojo(tweetJson)
    }

    for (ts <- tss) {
      val key = QueryIdCache.get(ts.queryString).get
      Logger.info(s"using key = " + key)
      val jtweet = Json.toJson(Tweet(ts.dateTime.getMillis/1000, key, ts.count))
      val messageStr: String = Json.stringify(jtweet)
      producer.send(messageStr, key)
      Logger.info(s"persisted to partition = " + key + " message = " + messageStr)
    }

  }

  override def receive: Actor.Receive = {
    case k: KafkaConnect => {
      val producer = new KafkaProducer(k.topic, k.brokerlist)
      producerMap += (k.topic -> producer)
      Logger.info(s"added kafka topic = " + k.topic)
    }

    case PersistenceMsg(Refresh3(), j: JsValue) => {
      persist(j, producerMap.get(Refresh3().key).get)
    }

    case PersistenceMsg(Refresh30(), j: JsValue) => {
      persist(j, producerMap.get(Refresh30().key).get)
    }

    case PersistenceMsg(Refresh300(), j: JsValue) => {
      persist(j, producerMap.get(Refresh300().key).get)
    }

    case PersistenceMsg(Refresh1800(), j: JsValue) => {
      persist(j, producerMap.get(Refresh1800().key).get)
    }

    case PersistenceMsg(Refresh10800(), j: JsValue) => {
      persist(j, producerMap.get(Refresh10800().key).get)
    }

  }
}
