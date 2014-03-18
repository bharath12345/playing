package controllers.configuration

import _root_.models.Tables._
import play.api.mvc._
import securesocial.core.SecureSocial

/**
 * Created by bharadwaj on 05/03/14.
 */
object ConfigurationApplication extends Controller with SecureSocial {

  def index = SecuredAction { implicit request =>
    Ok(views.html.configuration(Users.all))
  }
}
