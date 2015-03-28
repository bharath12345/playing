package controllers

import play.api._
import play.api.mvc._
import play.api.Play.current

//import in.bharathwrites.MyScalaEjbClient
//import in.bharathwrites.MyJavaEjbClient

/**
 * Created by bharadwaj on 06/03/14.
 */
object Application extends Controller {

  //val myjavaclient: MyJavaEjbClient = new MyJavaEjbClient
  //val myscalaclient: MyScalaEjbClient = new MyScalaEjbClient

  def index = Action {
    Redirect("/blog")
  }

  /*def scalahello = Action {
    Ok(myscalaclient.getGoodMorning)
  }

  def javahello = Action {
    Ok(myjavaclient.getHelloMsg)
  }*/

  def login = Action {
    Redirect("/configuration/login")
  }
}
