import akka.actor.{Props, ActorSystem}
import play.api._
import play.api.mvc._
import play.api.mvc.Results._
import twitter.{TweetProcessor, TweetStreamerActor, OAuthTwitterAuthorization}

/**
 * Created by bharadwaj on 29/01/14.
 */
object Global extends GlobalSettings {

  override def onStart(app: Application) {
    Logger.info("Application has started")

    val system = ActorSystem()
    val processor = system.actorOf(Props(new TweetProcessor))
    val stream = system.actorOf(Props(new TweetStreamerActor(TweetStreamerActor.twitterUri, processor) with OAuthTwitterAuthorization))
    stream ! "scala"
  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
  }

  /*

  override def onError(request: RequestHeader, ex: Throwable) = {
    InternalServerError(
      views.html.errorPage(ex)
    )
  }

  override def onHandlerNotFound(request: RequestHeader): Result = {
    NotFound(
      views.html.notFoundPage(request.path)
    )
  }

  override def onBadRequest(request: RequestHeader, error: String) = {
    BadRequest("Bad Request: " + error)
  }

  */
}
