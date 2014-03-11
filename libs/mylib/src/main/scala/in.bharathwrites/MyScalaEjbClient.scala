package in.bharathwrites

import javaeetutorial.helloservice.ejb.HelloService
import javax.naming.{InitialContext, Context}

class MyScalaEjbClient {

  def getGoodMorning: String = {
    val ctx: Context = new InitialContext
    var hs: HelloService = ctx.lookup("java:global/helloservice/HelloServiceBean!javaeetutorial.helloservice.ejb.HelloService").asInstanceOf[HelloService]
    hs.sayHello("good morning")
    "good morning"
  }

}