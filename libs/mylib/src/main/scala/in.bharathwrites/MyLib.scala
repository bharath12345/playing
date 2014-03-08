package in.bharathwrites

import javax.ejb.EJB
import javaeetutorial.helloservice.ejb.HelloService

object MyScalaEjbClient {

  @EJB(lookup="java:app/helloservice/HelloServiceBean!javaeetutorial.helloservice.ejb.HelloService")
   var helloservice: HelloService = null



}