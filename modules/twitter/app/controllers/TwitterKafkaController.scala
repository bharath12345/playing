package controllers.twitter

import play.api.mvc.{Action, Controller}
import _root_.kafka.KafkaConsumer
import kafka.KafkaConsumer
import java.util.UUID
import play.api.Logger

/**
 * Created by bharadwaj on 01/04/14.
 */
object TwitterKafkaController extends Controller {

  def readAsKafkaConsumer = Action {
    val testMessage = UUID.randomUUID().toString
    val testTopic = UUID.randomUUID().toString
    val groupId_1 = UUID.randomUUID().toString
    val groupId_2 = UUID.randomUUID().toString

    val consumer1 = new KafkaConsumer(testTopic,groupId_1,"192.168.86.5:2181")

    def exec1(binaryObject: Array[Byte]) = {
      val message1 = new String(binaryObject)
      Logger.info("testMessage 1 = " + testMessage + " and consumed message 1 = " + message1)
      consumer1.close()
    }



    Ok("Kafka consumer not yet implemented fully!")
  }

}
