//import configuration.ConfigurationGlobal
//import twitter.TwitterGlobal
import blog.BlogGlobal
import play.api._

/**
 * Created by bharadwaj on 29/01/14.
 */
object Global extends GlobalSettings {

  override def onStart(app: Application) {
    Logger.info("Application has started")
    BlogGlobal.onStart
    //ConfigurationGlobal.onStart
    //TwitterGlobal.onStart
  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
    BlogGlobal.onStop
    //ConfigurationGlobal.onStop
    //TwitterGlobal.onStop
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