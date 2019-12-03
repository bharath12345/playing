package controllers.blog

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}

/**
 * Created by bharadwaj on 27/01/14.
 */
@Singleton
class About @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def books = Action {
    Ok(views.html.self.books())
  }

  def current = Action {
    Ok(views.html.self.current())
  }

  def education = Action {
    Ok(views.html.self.education())
  }

  def movies = Action {
    Ok(views.html.self.movies())
  }

  def philosophy = Action {
    Ok(views.html.self.philosophy())
  }

  def travel = Action {
    Ok(views.html.self.travel())
  }

  def work = Action {
    Ok(views.html.self.work())
  }

}
