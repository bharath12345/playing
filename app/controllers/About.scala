package controllers

import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}

import javax.inject.{Inject, Singleton}

/** Created by bharadwaj on 27/01/14.
  */
@Singleton
class About @Inject() (cc: ControllerComponents)
    extends AbstractController(cc) {

  def myself = Action {
    Ok(Json.obj("content" -> "Scala Play React Seed!"))
  }

  def books = Action {
    Ok(Json.obj("content" -> "Scala Play React Seed!"))
  }

  def movies = Action {
    Ok(Json.obj("content" -> "Scala Play React Seed!"))
  }

}
