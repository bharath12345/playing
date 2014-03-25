import play.api.Logger

/**
 * Created by bharadwaj on 25/03/14.
 */
object ConfigurationGlobal {

  def onStart() = {
    Logger.info("Configuration module has started")
  }

  def onStop() {
    Logger.info("Configuration module shutdown...")
  }

}
