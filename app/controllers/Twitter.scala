package controllers

import play.api.mvc._
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.client.methods.{HttpPost, HttpGet}
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer
import org.apache.commons.io.IOUtils
import play.api._
import org.apache.http.HttpResponse
import org.apache.http.message.BasicNameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import scala.collection.JavaConversions
import twitter._
import akka.actor.{Props, ActorSystem}
import play.api.libs.iteratee.Enumerator
import java.io.ByteArrayInputStream

import scala.concurrent._
import ExecutionContext.Implicits.global

/**
 * Created by bharadwaj on 27/01/14.
 */
object Twitter extends Controller {

  val client = new DefaultHttpClient()
  val consumer = new CommonsHttpOAuthConsumer(Credentials.ck, Credentials.cs)
  consumer.setTokenWithSecret(Credentials.at, Credentials.as)

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

  def go(query: String) = Action {


    val system = ActorSystem()
    val processor = system.actorOf(Props(new TweetProcessor))
    val stream = system.actorOf(Props(new TweetStreamerActor(TweetStreamerActor.twitterUri, processor) with OAuthTwitterAuthorization))
    stream ! query

    Ok.chunked(
      Enumerator.fromStream(new ByteArrayInputStream(Cache.tstream.toByteArray)).andThen(Enumerator.eof)
      //Enumerator("kiki", "foo", "bar").andThen(Enumerator.eof)
    ).as("text/html")
  }
}
