package blog

import com.typesafe.config.ConfigFactory

/**
 * Created by bharadwaj on 11/04/14.
 */
trait Configuration {

  val config = ConfigFactory.load()

  //lazy val bonsaiHost = config.getString("bonsai.host")

}
