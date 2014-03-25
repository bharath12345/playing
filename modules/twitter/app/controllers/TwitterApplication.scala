package controllers.twitter

import play.api.mvc._
import play.api._
import play.api.libs.json.JsValue

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
      Ok(views.html.dashboard(Query.getStubs)(wsURL))
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
      Ok(views.html.dashboard(Query.getStubs)(wsUrl))
  }

  def live(period: Int) = WebSocket.async[JsValue] {
    request =>
      Statistics.attach(Refresh(period))
  }

  def sendmail = Action {
    EmailNotifier.sendMail
    Ok("sent something!")
  }

}
