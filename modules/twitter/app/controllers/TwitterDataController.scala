package controllers.twitter

import play.api.mvc.{Action, Controller}
import scala.slick.jdbc.JdbcBackend._
import scala.slick.jdbc.meta.MTable
import models.twitter.{ThreeSecDAO, QueryStringDAO}
import twitter.Configuration


/**
 * Created by bharadwaj on 25/03/14.
 */
object TwitterDataController extends Controller with Configuration {

  def create = Action {
    // init Database instance
    val db = Database.forURL(url = "jdbc:postgresql://%s:%d/%s".format(dbHost, dbPort, dbName),
      user = dbUser, password = dbPassword, driver = "org.postgresql.Driver")

    // create tables if not exist
    db.withSession { implicit session: Session =>
      if (!MTable.getTables.list.exists(_.name.name == "QueryStrings"))
        QueryStringDAO.create
      if (!MTable.getTables.list.exists(_.name.name == "ThreeSec"))
        ThreeSecDAO.create
    }

    Ok("table creation succeeded")
  }
}
