package controllers.blog

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}

/**
 * Created by bharadwaj on 27/01/14.
 */
@Singleton
class About @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def myself = Action {
    Ok(views.html.myself())
  }

  def books = Action {
    Ok(views.html.myself())
  }

  def movies = Action {
    Ok(views.html.myself())
  }

}
