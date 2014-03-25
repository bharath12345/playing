package models.twitter

import org.joda.time.DateTime
import scala.slick.driver.{PostgresDriver, JdbcProfile}
import com.github.tototoshi.slick.PostgresJodaSupport._
import scala.slick.driver.PostgresDriver.simple._

/**
 * Created by bharadwaj on 25/03/14.
 */

sealed abstract class TweetsAggregated (dateTime: DateTime, queryString: Long, count: Long)

case class ThreeSec (dateTime: DateTime, queryString: Long, count: Long)
  extends TweetsAggregated(dateTime, queryString, count)

case class ThirtySec (dateTime: DateTime, queryString: Long, count: Long)
  extends TweetsAggregated(dateTime, queryString, count)

case class FiveMin (dateTime: DateTime, queryString: Long, count: Long)
  extends TweetsAggregated(dateTime, queryString, count)

case class ThirtyMin (dateTime: DateTime, queryString: Long, count: Long)
  extends TweetsAggregated(dateTime, queryString, count)

case class ThreeHour (dateTime: DateTime, queryString: Long, count: Long)
  extends TweetsAggregated(dateTime, queryString, count)

class ThreeSecDAO(tag: Tag) extends Table[ThreeSec](tag, "ThreeSec") {

  def dateTime      = column[DateTime]("datetime", O.Nullable)
  def queryStringId = column[Long]    ("queryStringId")
  def counter       = column[Long]    ("counter")

  def queryString = foreignKey("queryStringId", queryStringId, QueryStringDAO.QueryStrings)(_.id)

  def * = (dateTime, queryStringId, counter) <> (ThreeSec.tupled, ThreeSec.unapply _)

}

object ThreeSecDAO {

  val ThreeSecData = TableQuery[ThreeSecDAO]

  def create(implicit session: Session) = ThreeSecData.ddl.create

  def findAll(implicit session: Session): List[ThreeSec] = (for{b <- ThreeSecData} yield b).list

  def insert(tsd: ThreeSec)(implicit session: Session) = ThreeSecData.insert(tsd)

  def getLastHour() = {}
  // and more such getters required
  def delete()(implicit session: Session) = {  }

}
