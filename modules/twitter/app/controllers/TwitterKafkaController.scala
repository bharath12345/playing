package controllers.twitter

import play.api.mvc.{Action, Controller}
import _root_.kafka.KafkaConsumer
import kafka.KafkaConsumer
import java.util.UUID
import play.api.Logger
import models.Refresh3

/**
 * Created by bharadwaj on 01/04/14.
 */
object TwitterKafkaController extends Controller {

  def readAsKafkaConsumer = Action {
    val testMessage = UUID.randomUUID().toString
    val topicName = Refresh3().key
    val groupId_1 = UUID.randomUUID().toString
    //val groupId_2 = UUID.randomUUID().toString

    val consumer1 = new KafkaConsumer(topicName, groupId_1, "localhost:2181")
    val data: String = consumer1.read()

    Ok(data)
  }

}
