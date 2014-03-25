package controllers.twitter

import play.api.mvc._
import org.apache.http.HttpResponse
import org.apache.http.client.methods.{HttpPost, HttpGet}
import org.apache.http.client.entity.UrlEncodedFormEntity
import play.api.Logger
import org.apache.commons.io.IOUtils
import org.apache.http.message.BasicNameValuePair
import scala.collection.JavaConversions
import org.apache.http.impl.client.DefaultHttpClient
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer
import twitter.TwitterCredentials

/**
 * Created by bharadwaj on 25/03/14.
 */
object TwitterQueryApiController extends Controller {

  val client = new DefaultHttpClient()
  val consumer = new CommonsHttpOAuthConsumer(TwitterCredentials.ck, TwitterCredentials.cs)
  consumer.setTokenWithSecret(TwitterCredentials.at, TwitterCredentials.as)

  def twGetRequestor(url: String): HttpResponse = {
    val request = new HttpGet(url)
    consumer.sign(request)
    client.execute(request)
  }

  def twPostRequestor(url: String, entity: UrlEncodedFormEntity): HttpResponse = {
    val request = new HttpPost(url)
    request.setEntity(entity);
    consumer.sign(request)
    client.execute(request)
  }

  def rawResponse(response: HttpResponse) = {
    Logger.debug(response.getStatusLine().getStatusCode().toString)
    Ok(IOUtils.toString(response.getEntity().getContent()))
  }

  def followers = Action {
    request =>
      val response = twGetRequestor("https://api.twitter.com/1.1/followers/ids.json?cursor=-1&screen_name=bharathtalks")
      rawResponse(response)
  }

  def mentions = Action {
    request =>
      val response = twGetRequestor("https://api.twitter.com/1.1/statuses/mentions_timeline.json?count=1")
      rawResponse(response)
  }

  def sample = Action {
    request =>

      val nvps = List(new BasicNameValuePair("", ""))
      val snvps = JavaConversions.seqAsJavaList(nvps)
      val uefe = new UrlEncodedFormEntity(snvps)

      val response = twPostRequestor("https://stream.twitter.com/1.1/statuses/sample.json", uefe)
      rawResponse(response)
  }

}
