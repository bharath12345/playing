package in.bharathwrites.mylib

import javax.ejb.EJB
//import org.glassfish.javaeetutorial.helloservice.ejb.HelloService

class MyLib {
  println(s"In my lib!")

  @EJB(lookup="java:app/helloservice/HelloServiceBean!javaeetutorial.helloservice.ejb.HelloService")
  var helloservice = null



}