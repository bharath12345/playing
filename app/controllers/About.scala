package controllers

import play.api.mvc.{Action, Controller}

/**
 * Created by bharadwaj on 27/01/14.
 */
object About extends Controller {

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
