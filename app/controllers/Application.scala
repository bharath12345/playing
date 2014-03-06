package controllers

import play.api._
import play.api.mvc._
import play.api.Play.current

/**
 * Created by bharadwaj on 06/03/14.
 */
object Application extends Controller {

  def index = Action {
    Redirect("/blog")
  }
}
