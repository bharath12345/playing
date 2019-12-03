package controllers.blog

import play.api.mvc._

import scala.collection.immutable.{HashMap, HashSet}
import blog.{BlogCategory, BlogIndex, BlogPost, BlogTag}
import javax.inject.{Inject, Singleton}

@Singleton
class BlogApplication @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
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
  def blog(id: String) = Action {
    val filename = id + ".md"
    Ok(views.html.blog(id.replaceAll(".md", ""))
      (BlogPost.title.get(filename).get)
      (BlogPost.content.get(filename).get)
      (BlogPost.date.get(filename).get)
      (BlogPost.subheading.get(filename).get)
      (BlogPost.toc.get(filename).get))
  }

  /**
   *
   * @return
   */
  def tags = Action {
    Ok(views.html.tags(BlogTag.tagSet))
  }

  /**
   *
   * @param id
   * @return
   */
  def tag(id: String) = Action {
    var titleSet = new HashSet[String]
    var fileList = new HashMap[String, String]
    var dateMap = new HashMap[String, String]

    BlogTag.tagLineMap foreach {
      case (tagLine, file) => {
        if (tagLine.contains(id)) {
          val title = BlogTag.titleMap.get(file).get
          titleSet += title
          fileList += (title -> file.replace(".md", ""))
          dateMap += (title -> BlogTag.dateMap.get(file).get)
        }
      }
    }

    Ok(views.html.tag(id)(titleSet)(fileList)(dateMap))
  }

  /**
   *
   * @return
   */
  def categories = Action {
    Ok(views.html.categories(BlogCategory.categorySet))
  }

  /**
   *
   * @param id
   * @return
   */
  def category(id: String) = Action {
    var titleSet = new HashSet[String]
    var fileList = new HashMap[String, String]
    var dateMap = new HashMap[String, String]

    for (category <- BlogCategory.categorySet) {
      if (category.equals(id)) {
        val file = BlogCategory.categoryMap.get(category).get
        val title = BlogCategory.titleMap.get(file).get
        titleSet += title
        fileList += (title -> file.replace(".md", ""))
        dateMap += (title -> BlogCategory.dateMap.get(file).get)
      }
    }

    Ok(views.html.category(id)(titleSet)(fileList)(dateMap))
  }

  /**
   *
   * @return
   */
  def toc = Action {
    Ok(views.html.toc(BlogIndex.sortedTitleList)(BlogIndex.pathMap)(BlogIndex.dateMap))
  }

  /**
   *
   * @return
   */
  def search = TODO

}
