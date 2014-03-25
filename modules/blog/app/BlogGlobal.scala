import play.api.Logger

/**
 * Created by bharadwaj on 25/03/14.
 */
object BlogGlobal {

  def onStart() = {
    Logger.info("Blog module has started")
  }

  def onStop() {
    Logger.info("Blog module shutdown...")
  }

}
