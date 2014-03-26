package controllers.twitter

import play.api.mvc._
import play.api._
import play.api.libs.json.{JsNull, Json, JsValue}

import models.twitter._
import models.Refresh

import twitter._

import scala.concurrent._
import ExecutionContext.Implicits.global
import notifiers.EmailNotifier
import securesocial.core.SecureSocial


/**
 * Created by bharadwaj on 27/01/14.
 */
object TwitterApplication extends Controller with SecureSocial {

  def dashboard(query: String) = Action {
    implicit request =>
      Query.addToQuery(query)
      val streamer = StartStreamer.startStreamerProcessor(query)
      streamer ! query
      val wsURL: String = controllers.twitter.routes.TwitterApplication.live(0).webSocketURL()
      val historyUrl = controllers.twitter.routes.TwitterApplication.history(0).absoluteURL()
      Ok(views.html.dashboard(Query.getStubs)(wsURL)(historyUrl))
  }

  def elections(period: Int) = SecuredAction {
    implicit request =>

      Query.addToQuery("india")
      Query.addToQuery("modi")
      Query.addToQuery("rahul")
      Query.addToQuery("kejri")
      val query: String = Query.getQuery

      val streamer = StartStreamer.startStreamerProcessor(query)
      streamer ! query

      val wsUrl = controllers.twitter.routes.TwitterApplication.live(period).webSocketURL()
      val historyUrl = controllers.twitter.routes.TwitterApplication.history(period).absoluteURL()

      Ok(views.html.dashboard(Query.getStubs)(wsUrl)(historyUrl))
  }

  def live(period: Int) = WebSocket.async[JsValue] {
    request =>
      Statistics.attach(Refresh(period))
  }

  def sendmail = Action {
    EmailNotifier.sendMail
    Ok("sent something!")
  }

  def history(period: Int) = Action {
    val json: JsValue = Json.obj(
      "name" -> "Watership Down",
      "location" -> Json.obj("lat" -> 51.235685, "long" -> -1.309197),
      "residents" -> Json.arr(
        Json.obj(
          "name" -> "Fiver",
          "age" -> 4,
          "role" -> JsNull
        ),
        Json.obj(
          "name" -> "Bigwig",
          "age" -> 6,
          "role" -> "Owsla"
        )
      )
    )
    Ok(Json.stringify(json))
  }

}
