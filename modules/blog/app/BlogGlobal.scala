package blog

import play.api.Logger
//import elasticsearch.BlogIndexer

/**
 * Created by bharadwaj on 25/03/14.
 */
object BlogGlobal {

  def onStart = {
    Logger.info("Blog module has started")

    BlogIndex.setupIndexPage
    BlogPost.setupPosts
    BlogTag.setupTags
    BlogCategory.setupCategories

    //BlogIndexer.createIndex

  }

  def onStop = {
    //BlogIndexer.shutdown
    Logger.info("Blog module shutdown...")
  }

}
