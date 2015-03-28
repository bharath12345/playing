package events

import securesocial.core._
import play.api.mvc.{Session, RequestHeader, Request}
import securesocial.core.LoginEvent
import securesocial.core.LogoutEvent
import play.Logger

/**
 * Created by bharadwaj on 18/03/14.
 */
class MyEventListener(app: play.api.Application) extends EventListener {
  override def id: String = "my_event_listener"

  def onEvent(event: Event, request: RequestHeader, session: Session): Option[Session] = {
    val eventName = event match {
      case e: LoginEvent => "login"
      case e: LogoutEvent => "logout"
      case e: SignUpEvent => "signup"
      case e: PasswordResetEvent => "password reset"
      case e: PasswordChangeEvent => "password change"
    }
    Logger.info("traced %s event for user %s".format(eventName, event.user.fullName))
    None
  }
}
