package in.bharathwrites;

import javax.ejb.EJB;
import javaeetutorial.helloservice.ejb.HelloService;

class MyJavaEjbClient {

  @EJB(lookup="java:app/helloservice/HelloServiceBean!javaeetutorial.helloservice.ejb.HelloService")
   private HelloService helloservice;



}