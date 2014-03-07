package controllers.configuration

import play.api.mvc._
import models.Test

/**
 * Created by bharadwaj on 05/03/14.
 */
object Application extends Controller {

  def test = Action {
    request =>
      val test = new Test

      // DO NOT DEPEND ON Play Framework's methodology to do JPA
      // Instead, since the WAR is going to be anyway in the Application Container with JPA/Hibernate,
      //     make direct invocation. That will work out naturally in the Java EE environment
      // So, this would mean that Persistence related operations will be unavailable when you run
      //     play in standalone mode - but that will be ONLY for Persistence related operations and thus
      //     can be accommodated. And has to be!

      Ok("Got request [" + request + "]")
  }
}
