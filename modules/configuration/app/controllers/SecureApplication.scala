/*
package controllers

import play.api.mvc._

/**
 * Created by bharadwaj on 18/03/14.
 */

// 1) add the SecureSocial trait to your controller
object SecureApplication extends Controller with securesocial.core.SecureSocial {

  // 2) change Play's Action with SecuredAction
  def index = SecuredAction {
    implicit request =>
      Ok(views.html.index(request.user))
  }

  def page = UserAwareAction {
    implicit request =>
      val userName = request.user match {
        case Some(user) => user.fullName
        case _ => "guest"
      }
      Ok("Hello %s".format(userName))
  }

  // you don't want to redirect to the login page for ajax calls so
  // adding a ajaxCall = true will make SecureSocial return a forbidden error
  // instead.
  def ajaxCall = SecuredAction(ajaxCall = true) {
    implicit request =>
    // return some json
  }

  // You can also use SecuredAction.async for async actions
  def asyncAjaxCall = SecuredAction(ajaxCall = true).async {
    implicit request =>
    // return a Future[SimpleResult] with some json
  }
}

*/