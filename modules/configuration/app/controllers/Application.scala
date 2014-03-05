package controllers.configuration

import play.api.mvc._
import javax.persistence.EntityManager
import play.db.jpa.JPA
import models.Test

/**
 * Created by bharadwaj on 05/03/14.
 */
object Application extends Controller {
  def test = Action {
    request =>
      val test = new Test
      val em: EntityManager = JPA.em()
      em.persist(test)

      Ok("Got request [" + request + "]")
  }
}
