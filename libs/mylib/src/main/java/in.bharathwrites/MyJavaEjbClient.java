package in.bharathwrites;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javaeetutorial.helloservice.ejb.HelloService;

import org.apache.log4j.Logger;

public class MyJavaEjbClient {

    private static final Logger logger = Logger.getLogger(MyJavaEjbClient.class.getName());

    public String getHelloMsg() {
        HelloService hs = null;
        try {
            Context ctx = new InitialContext();
            hs = (HelloService) ctx.lookup("java:global/helloservice/HelloServiceBean!javaeetutorial.helloservice.ejb.HelloService");
        } catch (NamingException e) {
            logger.info(e.getExplanation());
        }

        if (hs == null) {
            return "Both hello-service and another-hello-service are null";
        } else {
            return "One of the lookups SUCCEEDEED";
        }
    }
}