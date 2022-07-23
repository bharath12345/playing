package controllers

import blog.{BlogIndex, BlogPost, BlogTag}
import play.api.libs.json.Json
import play.api.mvc._
import play.api.{Environment, Logging}

import javax.inject.{Inject, Singleton}

@Singleton
class BlogApplication @Inject()(cc: ControllerComponents, env: Environment) extends AbstractController(cc) with Logging {

  /**
   *
   * @return
   */
  def homepage = Action {
    val (content, size) = BlogIndex.setupIndexPage(env, 0)
    Ok(Json.obj("homepage" -> content))
  }

  /**
   *
   * @param id
   * @return
   */
  def blog(id: String) = Action {
    BlogPost.setupPosts(env, id) match {
      case Some(x) => Ok(Json.obj("blog" -> x))
      case None => NotFound(s"$id not found")
    }
  }

  /**
   *
   * @return
   */
  def tags = Action {
    Ok(Json.obj("blog" -> BlogTag.setupTags(env)))
  }

  def tag(name: String) = Action {
    Ok(Json.obj("blog" -> BlogTag.listBlogsForTag(env, name)))
  }

  def page(num: Int) = Action {
    val (content, size) = BlogIndex.setupIndexPage(env, num)
    Ok(Json.obj("page" -> content))
  }

}
