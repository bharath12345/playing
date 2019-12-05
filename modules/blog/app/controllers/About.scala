package controllers.blog

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}

/**
 * Created by bharadwaj on 27/01/14.
 */
@Singleton
class About @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def books = TODO

  def current = TODO

  def education = TODO

  def movies = TODO

  def philosophy = TODO

  def travel = TODO

  def work = TODO

}
