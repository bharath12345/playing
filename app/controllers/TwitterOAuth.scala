package controllers

import org.apache.http.client.HttpClient
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.client.methods.HttpGet
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer
import org.apache.commons.io.IOUtils
import play.Logger

/**
 * Created by bharadwaj on 27/01/14.
 */
object TwitterOAuth {

  val at = "262534519-OarcKBDjQMmZYGS9rU5CJSwdHAdiXs4eXJOoOCiA";
  val as = "9MBU2hcAKHQWBtEPE1ihFlM3IlLjVwN8uhUFhuGMsiws3";

  val ck = "Adqs6idtfSjWUp1LOLB2g";
  val cs = "OPQJ9RvKu3fwL8cirFJwMAHs5iof3aEaIy7hosCyI";

  def authenticate: String = {

    val consumer = new CommonsHttpOAuthConsumer(ck,cs);
    consumer.setTokenWithSecret(at, as);

    val request = new HttpGet("https://api.twitter.com/1.1/followers/ids.json?cursor=-1&screen_name=bharathtalks");
    //val re = new HttpsGet("xyz")
    consumer.sign(request);
    val client = new DefaultHttpClient();
    val response = client.execute(request);

    Logger.debug(response.getStatusLine().getStatusCode().toString);
    IOUtils.toString(response.getEntity().getContent());
  }
}
