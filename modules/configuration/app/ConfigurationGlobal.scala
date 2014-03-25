package configuration

import models.configuration.Tables._
import play.api.Logger
import scala.slick.jdbc.JdbcBackend._
import scala.slick.jdbc.meta.MTable

/**
 * Created by bharadwaj on 25/03/14.
 */
object ConfigurationGlobal extends Configuration {

  def onStart = {
    Logger.info("Configuration module has started")

    createTables
  }

  def createTables = {
    val db = Database.forURL(url = "jdbc:postgresql://%s:%d/%s".format(dbHost, dbPort, dbName),
      user = dbUser, password = dbPassword, driver = "org.postgresql.Driver")

    // create tables if not exist
    db.withSession { implicit session: Session =>
      if (!MTable.getTables.list.exists(_.name.name == "user"))
        Users.create
      if (!MTable.getTables.list.exists(_.name.name == "token"))
        Tokens.create
    }
  }

  def onStop {
    Logger.info("Configuration module shutdown...")
  }

}
