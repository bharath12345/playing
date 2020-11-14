package controllers.blog

import blog.{BlogIndex, BlogPost}
import javax.inject.{Inject, Singleton}
import play.api.mvc._
import play.api.{Environment, Logging}

@Singleton
class BlogApplication @Inject()(cc: ControllerComponents, env: Environment) extends AbstractController(cc) with Logging {

  /**
   *
   * @return
   */
  def index = Action {
    Ok(views.html.index(BlogIndex.setupIndexPage(env)))
  }

  /**
   *
   * @param id
   * @return
   */
  def blog(id: String) = Action {
    BlogPost.setupPosts(env, id) match {
      case Some(x) => Ok(views.html.blog(x))
      case None => NotFound(s"$id not found")
    }
  }

  /**
   *
   * @return
   */
  def tags = Action {
    Ok(views.html.tags())
  }

  /**
   *
   * @return
   */
  def categories = Action {
    Ok(views.html.categories())
  }

}
