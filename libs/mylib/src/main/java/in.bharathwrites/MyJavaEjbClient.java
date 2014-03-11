package in.bharathwrites;

import javax.ejb.EJB;
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

            try {
                hs = (HelloService) ctx.lookup("java:global/helloservice/HelloServiceBean");
            } catch (NamingException ne) {
                logger.info(ne.getExplanation());
            }

            try {
                if (hs == null) {
                    logger.info("first lookup attempt failed");
                    hs = (HelloService) ctx.lookup("java:global/helloservice/HelloServiceBean!javaeetutorial.helloservice.ejb.HelloService");
                }
            } catch (NamingException ne) {
                logger.info(ne.getExplanation());
            }

            try {
                if (hs == null) {
                    logger.info("second lookup attempt failed");
                    hs = (HelloService) ctx.lookup("java:app/helloservice/HelloServiceBean");
                }
            } catch (NamingException ne) {
                logger.info(ne.getExplanation());
            }

            try {
                if (hs == null) {
                    logger.info("third lookup attempt failed");
                    hs = (HelloService) ctx.lookup("java:app/helloservice/HelloServiceBean!javaeetutorial.helloservice.ejb.HelloService");
                }
            } catch (NamingException ne) {
                logger.info(ne.getExplanation());
            }

            if (hs == null) {
                if (ctx.lookup("java:global/ejbremote-core-ear/ejbremote-core-ejb/HelloServiceBean") != null) {
                    logger.info("SOMETHING HAPPENED");
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        if (hs == null) {
            return "Both hello-service and another-hello-service are null";
        } else {
            return "One of the lookups SUCCEEDEED";
        }
    }
}