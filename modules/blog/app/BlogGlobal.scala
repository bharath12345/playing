package blog

import play.api.{Application, Environment, Logging}

/**
 * Created by bharadwaj on 25/03/14.
 */
object BlogGlobal extends Logging {

  def init(env: Environment): Unit = {
    logger.info("Blog module has started")
    BlogIndex.setupIndexPage(env)
    BlogPost.setupPosts(env)
    BlogTag.setupTags(env)
    BlogCategory.setupCategories(env)
    //BlogIndexer.createIndex
  }
}
