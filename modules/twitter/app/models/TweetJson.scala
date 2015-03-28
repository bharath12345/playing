package models

import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.Logger
import models.twitter.{ThreeSec, QueryStringDAO, QueryString}
import org.joda.time.DateTime
import play.api.libs.json.JsSuccess
import models.twitter.ThreeSec
import models.twitter.QueryString
import scala.slick.jdbc.JdbcBackend._
import play.api.libs.json.JsSuccess
import models.twitter.ThreeSec
import models.twitter.QueryString
import scala.Some

/**
 * Created by bharadwaj on 27/03/14.
 */
trait TweetJson {

  case class TweetCounter(stub: String, tweets: Long)
  case class TweetJson(timestamp: Long, period: Int, tc: Seq[TweetCounter])

  implicit val tcReads: Reads[TweetCounter] = (
    (JsPath \ "stub").read[String] and
      (JsPath \ "tweets").read[Long]
    )(TweetCounter.apply _)

  implicit val tjReads: Reads[TweetJson] = (
    (JsPath \ "timestamp").read[Long] and
      (JsPath \ "period").read[Int] and
      (JsPath \ "values").read[Seq[TweetCounter]]
    )(TweetJson.apply _)

  val QueryStringCache = scala.collection.mutable.Map[String, Long]()
  val QueryIdCache = scala.collection.mutable.Map[Long, String]()

  def validate(j: JsValue): JsResult[TweetJson] = j.validate[TweetJson]

  def retrievePojo(tweetJson: JsResult[TweetJson])(implicit session: Session) = {
    var tseq = scala.collection.mutable.Seq[ThreeSec]()
    tweetJson match {
      case s: JsSuccess[TweetJson] => {
        val tj: TweetJson = s.get
        for(tc <- tj.tc) {
          Logger.info(s"timestamp = " + tj.timestamp + " stub = " + tc.stub + " value = " + tc.tweets)
          val id: Option[Long] = QueryStringCache.get(tc.stub)
          val nid = id match {
            case Some(i) => i
            case None => {
              val qs:QueryString = QueryString(0, tc.stub)
              val newid = QueryStringDAO.findOrInsert(qs)
              QueryStringCache += (qs.queryString -> newid)
              QueryIdCache += (newid -> qs.queryString)
              newid
            }
          }
          val ts:ThreeSec = ThreeSec(new DateTime(tj.timestamp), nid, tc.tweets)
          tseq = tseq :+ ts
        }
      }
      case e: JsError => println("Errors: " + JsError.toFlatJson(e).toString())
    }
    tseq
  }
}
