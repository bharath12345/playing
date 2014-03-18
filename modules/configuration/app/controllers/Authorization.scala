package controllers

import securesocial.core._
import play.api.mvc.Controller

/**
 * Created by bharadwaj on 18/03/14.
 */

trait Authorization {
  def isAuthorized(user: Identity): Boolean
}

case class WithProvider(provider: String) extends Authorization {
  def isAuthorized(user: Identity) = {
    user.identityId.providerId == provider
  }
}


  //def myAction = SecuredAction(WithProvider("twitter")) { implicit request =>
    // do something here
  //}
