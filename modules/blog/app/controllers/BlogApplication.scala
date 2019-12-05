package controllers.blog

import play.api.mvc._

import scala.collection.immutable.{HashMap, HashSet}
import blog.{BlogCategory, BlogGlobal, BlogIndex, BlogPost, BlogTag}
import javax.inject.{Inject, Provider, Singleton}
import play.api.{Application, Environment, Logging}

@Singleton
class BlogApplication @Inject()(cc: ControllerComponents, env: Environment) extends AbstractController(cc) with Logging {

  BlogGlobal.init(env)

  /**
   *
   * @return
   */
  def index = TODO

  /**
   *
   * @param id
   * @return
   */
  def blog(id: String) = TODO

  /**
   *
   * @return
   */
  def tags = TODO

  /**
   *
   * @param id
   * @return
   */
  def tag(id: String) = TODO

  /**
   *
   * @return
   */
  def categories = TODO

  /**
   *
   * @param id
   * @return
   */
  def category(id: String) = TODO

  /**
   *
   * @return
   */
  def toc = TODO

  /**
   *
   * @return
   */
  def search = TODO

}
