package controllers.configuration

import _root_.models.configuration.Tables._
import play.api.mvc._
import securesocial.core.SecureSocial
import scala.language.reflectiveCalls

/**
 * Created by bharadwaj on 05/03/14.
 */
object ConfigurationApplication extends Controller with SecureSocial {

  def users = SecuredAction { implicit request =>
    Ok(views.html.configuration(Users.all))
  }

}
