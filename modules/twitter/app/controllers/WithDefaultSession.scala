package controllers

import play.api.db.DB
import scala.slick.driver.PostgresDriver.simple._

/**
 * Created by bharadwaj on 25/03/14.
 */
trait WithDefaultSession {

  import play.api.Play.current

  def withSession[T](block: (Session => T)) = {
    val databaseURL = play.api.Play.current.configuration.getString("db.default.url").get
    val databaseDriver = play.api.Play.current.configuration.getString("db.default.driver").get
    val databaseUser = play.api.Play.current.configuration.getString("db.default.user").getOrElse("")
    val databasePassword = play.api.Play.current.configuration.getString("db.default.password").getOrElse("")

    /*val database = Database.forURL(url = databaseURL,
      driver = databaseDriver,
      user = databaseUser,
      password = databasePassword)*/

    val database = Database.forDataSource(DB.getDataSource())

    database withSession {
      session =>
        block(session)
    }
  }
}