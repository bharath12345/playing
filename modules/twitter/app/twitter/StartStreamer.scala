package twitter

import akka.actor.{Props, ActorSystem, ActorRef}
import play.api.Logger

/**
 * Created by bharadwaj on 25/03/14.
 */
object StartStreamer {

  var processors: Map[String, ActorRef] = Map()
  var streamers: Map[String, ActorRef] = Map()

  def startStreamerProcessor(query: String): ActorRef = {
    val processor: ActorRef = processors.find(_._1 == query).map(_._2) match {
      case Some(actor) => {
        Logger.info(s"using existing processor. actor = $query")
        actor
      }
      case None => {
        Logger.info(s"creating NEW processor. actor = $query")
        val system = ActorSystem()
        val newprocessor = system.actorOf(Props(new TweetProcessor(query)), name = s"processor-$query")
        processors += (query -> newprocessor)
        newprocessor
      }
    }

    val streamer: ActorRef = streamers.find(_._1 == query).map(_._2) match {
      case Some(actor) => {
        Logger.info(s"using existing streamer. actor = $query")
        actor
      }
      case None => {
        Logger.info(s"creating NEW streamer. actor = $query")
        val system = ActorSystem()
        val streamer = system.actorOf(Props(new TweetStreamerActor(TweetStreamerActor.twitterUri, processor) with OAuthTwitterAuthorization),
          name = s"streamer-$query")
        streamers += (query -> streamer)
        streamer
      }
    }
    streamer
  }

}
