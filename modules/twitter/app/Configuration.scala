package twitter

import com.typesafe.config.ConfigFactory
import java.net.URI
import scala.slick.jdbc.JdbcBackend._
import com.tempodb.client.{Client, ClientBuilder}

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
  lazy val dbDefaultUrl = config.getString("db.default.url")

  lazy val dbUri = new URI(dbDefaultUrl)

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
  lazy val db = Database.forURL(url = "jdbc:postgresql://%s:%d/%s".format(dbHost, dbPort, dbName),
    user = dbUser, password = dbPassword, driver = "org.postgresql.Driver")

  lazy val tempoDbKey = config.getString("tempodb.apikey")

  lazy val tempoDbSecret = config.getString("tempodb.apisecret")

  lazy val tempoDbHost = config.getString("tempodb.host")

  lazy val tempoDbPort = config.getInt("tempodb.port")

  lazy val tempoClient: Client = new ClientBuilder()
    .key(tempoDbKey)
    .secret(tempoDbSecret)
    .host(tempoDbHost)
    .port(tempoDbPort)
    .secure(true)
    .build();

}
