package models.twitter

import scala.slick.driver.{JdbcProfile}
import scala.slick.driver.PostgresDriver.simple._

/**
 * Created by bharadwaj on 25/03/14.
 */

case class QueryString(id: Long, queryString: String)

class QueryStringDAO(tag: Tag) extends Table[QueryString](tag, "QueryStrings") {

  def id       = column[Long]  ("queryStringId", O.PrimaryKey, O.AutoInc)
  def queryString = column[String]("queryString")

  def * = (id, queryString) <> (QueryString.tupled, QueryString.unapply _)
  //def pk = primaryKey("pk_qs", (id, queryString))
  def idx = index("idx_qs", queryString, unique = true)
}

object QueryStringDAO {

  val QueryStrings = TableQuery[QueryStringDAO]

  private val queryStringsAutoInc = QueryStrings returning QueryStrings.map(_.id) into { case (b, id) => b.copy(id = id) }

  def create(implicit session: Session) = QueryStrings.ddl.create

  def findById(id: Long)(implicit session: Session): Option[QueryString] = (for {
    b <- QueryStrings
    if b.id === id
  } yield b).firstOption

  def findOrInsert(qs: QueryString)(implicit session: Session): Long = {
    val qqs: Option[QueryString] = (for {
      b <- QueryStrings
      if b.queryString === qs.queryString
    } yield b).firstOption

    val id = qqs match {
      case Some(querys) => querys.id
      case None => insert(qs).id
    }

    id
  }

  def findAll(implicit session: Session): List[QueryString] = (for{b <- QueryStrings} yield b).list

  def insert(queryString: QueryString)(implicit session: Session): QueryString = queryStringsAutoInc.insert(queryString)

}
