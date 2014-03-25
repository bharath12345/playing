package twitter

import com.typesafe.config.ConfigFactory
import java.net.URI
import scala.slick.jdbc.JdbcBackend._

/**
 * Holds service configuration settings.
 */
trait Configuration {

  /**
   * Application config object from src/main/resources/application.conf
   */
  val config = ConfigFactory.load()

  /**
   * Database settings
   */
  var x = config.getString("db.default.url")

  lazy val dbUri = new URI(x)

  /** Database host name/address. */
  lazy val dbHost = dbUri.getHost()

  /** Database host port number. */
  lazy val dbPort = dbUri.getPort()

  /** Service database name. */
  lazy val dbName = dbUri.getPath().substring(1)

  /** User name used to access database. */
  lazy val dbUser = dbUri.getUserInfo().split(":")(0)

  /** Password for specified user and database. */
  lazy val dbPassword = dbUri.getUserInfo().split(":")(1)

  // init Database instance
  val db = Database.forURL(url = "jdbc:postgresql://%s:%d/%s".format(dbHost, dbPort, dbName),
    user = dbUser, password = dbPassword, driver = "org.postgresql.Driver")


}
