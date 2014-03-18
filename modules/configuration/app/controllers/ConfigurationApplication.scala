package controllers.configuration

import _root_.models.configuration.Tables._
import play.api.mvc._
import securesocial.core.SecureSocial
import scala.slick.driver.{JdbcProfile,PostgresDriver}
import scala.slick.jdbc.meta.MTable
import scala.slick.jdbc.JdbcBackend.{Database, Session}
import configuration.Configuration

/**
 * Created by bharadwaj on 05/03/14.
 */
object ConfigurationApplication extends Controller with SecureSocial with Configuration {

  def index = SecuredAction { implicit request =>
    Ok(views.html.configuration(Users.all))
  }

  def create = Action {
    // init Database instance
    val db = Database.forURL(url = "jdbc:postgresql://%s:%d/%s".format(dbHost, dbPort, dbName),
      user = dbUser, password = dbPassword, driver = "org.postgresql.Driver")

    // create tables if not exist
    db.withSession { implicit session: Session =>
      if (!MTable.getTables.list.exists(_.name.name == "user"))
        Users.create
      if (!MTable.getTables.list.exists(_.name.name == "token"))
        Tokens.create
    }

    Ok("table creation succeeded")
  }
}
