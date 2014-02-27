package models

/**
 * Created by bharadwaj on 26/02/14.
 */
object Query {

  var query: String = ""
  var stubs: Array[String] = Array()

  def addToQuery(stub: String) {
    stubs +:= stub
    query += stub + ","
  }

  def getQuery: String = query

  def getStubs: Array[String] = stubs

}
