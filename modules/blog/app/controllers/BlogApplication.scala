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
  def index = Action {
    Ok(views.html.index(BlogIndex.sortedTitleList)(BlogIndex.fileMap)(BlogIndex.dateMap)(BlogIndex.contentMap))
  }

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
   * @return
   */
  def categories = TODO

}
