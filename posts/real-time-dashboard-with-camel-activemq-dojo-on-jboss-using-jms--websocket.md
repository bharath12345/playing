{{{
    "title": "Real Time Dashboard with Camel, ActiveMQ & Dojo... On JBoss7 and using JMS & WebSocket",
    "tags" : [ "java", "websocket", "jboss", "visualization" ],
    "category" : "technology",
    "date" : "08-01-2013",
    "description" : ""
}}}

I have built real-time 'stock-ticker' like dashboards. There are many ways to build them. Few months ago I had the opportunity to design one freshly again for an enterprise product. I did a quick sweep at the different technology stacks that can be used to build a highly scalable (design/code and performance scalability) real-time dashboard. There are many technologies for real-time in the browser (like BlazeDS) that are either outdated or on their way out. I came across this very interesting [presentation](http://fusesource.com/apache-camel-conference-2012/videos/camelone-2012-charles-moulliard-video/), [code](https://github.com/FuseByExample/websocket-activemq-camel) and [blog](http://cmoulliard.blogspot.in/2012_04_01_archive.html) by Charles Moulliard which I found to be a very exciting design. So I sat down to extend what Charles had done to suit my usecase. I would recommend [this nice book](http://www.amazon.com/The-Definitive-Guide-HTML5-WebSocket/dp/1430247401) by Apress as a good introduction to the subject of WebSockets. But before getting to the real usecase and seeing why use Camel or ActiveMQ, here is a quick primer to the different techniques one could use to build a real-time dashboard.

### Primer of Different Techniques
#### 1. Polling Based
Ajax requires a client side request to get data to the browser. So the simplest solution is to buld a client side timer based poller. Maybe use JavaScript timers like setInterval or setTimeout (or wrappers from libraries). 

<table class="table table-bordered table-striped table-condensed bs-docs-grid">
	<tr>
		<td>Pro</td>
		<td>Con</td>
	</tr>
	<tr>
		<td>Simplicity</td>
		<td>If the data being polled is increasing or is large, continuous degradation in performance is natural as data is fetched and rendered each time. If the 'real-time' SLAs call for changes to be shown quickly (< 5 seconds), then continous polling on the client starts to weigh heavy on the data source. It could lead to continously running large number of SQLs. Above all, this would simply not scale if you expect a large number of users and/or large number or real-time data types.</td>
	</tr>
</table>

#### 2. Stateful and RESTful
Maintain 'states' at either server or client to reduce what is queried and transmit size. Actually there are two options,

* Client side stateful
* Server side stateful

But [REST mandates](http://en.wikipedia.org/wiki/Representational_state_transfer#Constraints) the following 2 constrains -

	Stateless
	The client–server communication is further constrained by no client context being stored on the server between requests. Each request from any client contains all of the information necessary to service the request, and any session state is held in the client.

	Cacheable
	As on the World Wide Web, clients can cache responses. Responses must therefore, implicitly or explicitly, define themselves as cacheable, or not, to prevent clients reusing stale or inappropriate data in response to further requests. Well-managed caching partially or completely eliminates some client–server interactions, further improving scalability and performance.

Am no expert in RESTful design. But I know for sure that many implementations (especially those which have *streaming* in their name) relax the stateless at server constraint. So, statefulness can go thus -

* **Client-side stateful**: Client asks for only the incremental. For example a timestamp based method could be adopted by the client to get the incrementals (by doing so the timestamp becomes the 'state'). There are some wonderful JavaScript frameworks that make state maintenance possible. One can use [BackboneJS](http://backbonejs.org) or Dojo's [Observable](http://dojotoolkit.org/reference-guide/1.9/dojo/store/Observable.html) pattern to build a store in the browser and update the UI only on the incremental changes. Combined with RESTful HTTP APIs on the server-side, one can build robust applications
* **Server-side stateful**: Server can respond with *only* the incremental when a request from the same client arrives. Server side HTTP API's publish incremental data of different types and filtering. A session handshake or client-subscription is required before the start (server has to maintain state for each client).

<table class="table table-bordered table-striped table-condensed bs-docs-grid">
	<tr>
		<td>Pro</td>
		<td>Con</td>
	</tr>
	<tr>
		<td>Since only the incremental 'delta' is in transit and re-rendered on the UI, these methods scale in performance. They are well suited for web applications where 3rd party developers could be using your data feed to build user interface or other real-timer services.</td>
		<td>Maintaining state can quickly become very complex. Multiple types of data, with different incrementals can lead to 'cache-mess'. It leads to many many caches and really big caches. User actions like filtering add considerable complexity to the underlying infra. And despite only incrementals being in transit, it is still a request-response system, making tight SLA's (<5 seconds refresh rate) quite a challenge.</td>
	</tr>
</table>

#### 3. Comet
Comet, Reverse-Ajax et al. are hacks and not solutions. The idea is that the browser makes an Ajax request to the server, which is kept open until the server has new data to send to the browser. Once the server has the event it wants to send, it sends it on this already open channel. And soon after getting a response the browser initiates a new long polling request in order to obtain subsequent events. Multiple frameworks exist to accomplish the job from both server and client side. But the technology is riddled with bugs, browser incompatibilities and is a total mess.

#### 4. WebSocket
Websockets are a new protocol. The protocol specifies for setting up of a full duplex communication channel between client and server on top of HTTP(S). The HTTP header from client side has a "upgrade" field set to *websocket* and "connection" field set to *upgrade*. All modern browsers support this by the new JavaScript API WebSocket(). So the question boils down to - whats the best way to handle these upgrade requests on the server side? There are upcoming frameworks like [Atmosphere](https://github.com/Atmosphere/atmosphere) which interoperate with popular existing server and client frameworks promising easy adoption.

### Usecase 
A real time *alerts* dashboard. In any monitoring/management/analytics system, events go through multiple stages before getting transformed into an alert needing to be displayed for concerned users. Event pipelines come in many types and JMS is not uncommon. The usecase here is of such a system where event processors pick events to evaluate and filter. The SLAs for critical alerts can be very small time-periods depending on the domain.

### Design
The image below shows the 5 components of the implementation of my usecase. The code is posted on GitHub [here](https://github.com/bharath12345/RealTimeDashboard).

**1. AsyncHttpClient**: This is just a data feed. In most data-center scenario's the data-feed to IT management/analytics/monitoring services is separated by a firewall. I use [Ning HTTP client](http://www.ning.com/code/2010/03/introducing-nings-asynchronous-http-client-library/) - it is based on the superb Jetty NIO2 implementation and works well with JBoss. For the prototype's sake, I have taken the data itself to be just the HTTP headers. It could be anything from the payload also. And it could be from other type of sources like SNMP etc

**2. AsyncHttpServer**: Camel provides a Jetty NIO2 based Async Server implementation. I use that to receive the client connections and pick the data (http headers in my case). 

**3. JMS Broker**: I use ActiveMQ. JBoss packages HornetQ natively. But ActiveMQ is by far the most popular JMS broker on planet earth. 

**4. Multiple JMS Topics**: The data receiver can publish the received data into a chosen JMS topic (depending on the data received). The first publish is of Serializable Java POJO. The receiver on this JMS topic picks the POJO, transforms it to JSON and publishes to a different set of JMS topic's just for JSON (this is not shown in the image below but can be seen in the code).

**5. Camel JMS to WebSocket Route**: Camel route is used to pick data from the JSON JMS topic and post it to both - WebSocket & log file - together. A final JSON level transformation can be applied in this stage if need be.

**6. JavaScript UI**: A JavaScript WebSocket() connects and waits for JSON messages to appear. Received messages are shown in a grid (Dojo's GridX actually)

![image](http://bharathwrites.in/images/camel-websocket/camel%20jms%20websocket.png)

### The Why's? 
#### 1. Why Apache Camel?
(1) I wanted to learn Camel (2) Apache Camel is brilliant for plumbing purposes between modules/services within an enterprise product. The number of supported components is dizzying. Despite the heavy sounding ESB word being thrown around with it I have found it quite easy to grasp and it just works like a charm!  

#### 2. Why ActiveMQ and not Camel's native JMS implementation?
One of my dear friends, [Sumanth](http://in.linkedin.com/in/sumanthn83), pointed this rather subtle mention on performance aspect in Camel's JMS page.

	http://camel.apache.org/jms.html
	
	The JMS component reuses Spring 2's JmsTemplate for sending messages. This is not ideal for use in a non-J2EE container and typically requires some caching in the JMS provider to avoid poor performance.If you intend to use Apache ActiveMQ as your Message Broker - which is a good choice as ActiveMQ rocks…
	
Further to this, I am slowly developing an aversion to everything Spring. I opine that it is better to avoid Spring in any new development project of scale. And Camel JMS is based on Spring. So better to use ActiveMQ directly.

#### 3. Why WebSocket?
Experts in RESTful design like [Bill Burke](http://bill.burkecentral.com/2012/02/28/web-sockets-a-disaster-in-waiting/) denounce WebSockets sharply. There are [others](http://www.infoq.com/news/2012/02/websockets-rest) who welcome it anyway. Personally, I like the idea of a full duplex channel on top of HTTP. I dont think WebSockets maybe a good idea for companies and applications to expose there data and services - which exactly is the usecase for RESTful. WebSockets quite beautifully fit within enterprise products/applications where services are consumed internally between modules/known-applications and are deployed in a distributed setup where they cross multiple DMZ. Along with the upcoming [draft of HTTP 2.0](http://apiux.com/2013/07/23/http2-0-initial-draft-released/) which will hopefully support -

* Binary
* Connections remain open so long as user stays on the page
* Multiple open streams 
* Priorities

having WebSockets will make HTTP a dependable channel for realtime!

#### 4. Why JBoss7?
In the world of open source Java, JBoss is simply the best application container around. I used the [Wildfly](http://wildfly.org) 8.0 Alpha3 for this prototype 

### How to use and results
1. A "mvn clean install" would build the EAR which should be deployed in JBoss 7+
2. From the JBoss JMX Console, use the firePostRequests() operation to send HTTP client side requests (com.bharath.http.client)

![image](http://bharathwrites.in/images/camel-websocket/jmx.png)

The snapshot of the dashboard UI -

![image](http://bharathwrites.in/images/camel-websocket/dashboard.png)

### My Conclusion!!
Asynchronous processing by pushing to multiple JMS topic's when combined with Apache Camel's routing and WebSocket capabilities can provide for building a truely fast and efficient events/alerts pipeline for a realtime alerts dashboard
