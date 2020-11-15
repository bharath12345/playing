package controllers.blog

import blog.{BlogTag, BlogIndex, BlogPost}
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
    val (content, size) = BlogIndex.setupIndexPage(env, 0)
    Ok(views.html.index(content, size))
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
    Ok(views.html.tags(BlogTag.setupTags(env)))
  }

  def tag(name: String) = Action {
    Ok(views.html.index(BlogTag.listBlogsForTag(env, name), -1))
  }

  def page(num: Int) = Action {
    val (content, size) = BlogIndex.setupIndexPage(env, num)
    Ok(views.html.index(content, size))
  }

}
