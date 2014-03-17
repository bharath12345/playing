package notifiers

/**
 * Created by bharadwaj on 17/03/14.
 */
import com.typesafe.plugin._
import play.api.Play.current

object EmailNotifier {
  def sendMail {
    val mail = use[MailerPlugin].email
    mail.setSubject("Mail test")
    mail.addRecipient("Bharath <bharath12345@gmail.com>","bharath12345@yahoo.com")
    mail.addFrom("Bharath <bharath12345@gmail.com>")

    //mail.setBcc(List("Dummy <example@example.org>", "Dummy2 <example@example.org>"):_*)
    mail.sendHtml("<html>html</html>")
  }
}
