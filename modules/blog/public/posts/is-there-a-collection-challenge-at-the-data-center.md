{{{
    "title": "System and Application health - Is there a data collection challenge at the DC?",
    "subheading": "",
    "tags" : [ "data-center", "APM" ],
    "category" : "technology",
    "date" : "08-06-2013",
    "description" : "",
    "toc": true
}}}

My champion-hacker friend Sumanth and I spent a little time few weeks ago digging to know if there was a data collection challenge for system and application health metrics at a typical small data center. Here is the little that we discovered...

But the self questioning was preceded by a phase where I came to know about open source monitoring tools like [Ganglia](http://ganglia.sourceforge.net/) and discovered that they were widely deployed. Ganglia in particular is very active as a development project and uses [Multicast](http://en.wikipedia.org/wiki/Multicast) for data transmission. It caches data at all nodes within a cluster. The collection station has to communicate to only one (any) node within a cluster. I asked myself what could be the rationale behind this design. And as I looked more deeply into the world of Ganglia I discovered the amazing attention to detail - to optimise data and time without compromising accuracy or extensibility. For those wishing to understand Ganglia, [this book](http://shop.oreilly.com/product/0636920025573.do) is a must read. The chapter on case-studies in this book makes for a truly fascinating read. [This paper](http://www.ittc.ku.edu/~niehaus/classes/750-s07/documents/ganglia-parallel-computing.pdf) also provides a very good intro.

And then there is SNMP. SNMP was built for monitoring. One small deficiency is that not all metrics are available through SNMP. In this blog I decided to keep SNMP aside and do the analysis. I present the numbers first and my inferences later. 

#### Usecase

Let me take the example of a data center for a small eCommerce startup, say, called "WebTraveller". Now, a little detail about this company -

* WebTraveller is a travel portal on the lines of Expedia but with a target market of few small-and-medium-sized enterprises in its region
* WebTraveller decided to build its web application using *Ruby* (which is going strong as No.2 on GitHub top languages - [https://github.com/languages](https://github.com/languages))
* For its many static pages and load-balancing, the IT folk at WebTraveller decided to do the time-tested thingy - use *Apache HTTPD or Nginix*
* WebTraveller has to interface with travel data providers (airlines, bus company's et al), payment gateways, advertisers etc. Let us assume a simple idealistic world where all this data comes through superbly designed RESTful interface. So, the  IT folk at WebTraveller decided to publish/subscribe to this RESTful external data interface through a *Java application*
* WebTraveller uses MySql or Postgres as its database to store user info etc
* Analytics is important for WebTraveller to run promotions, tune resources per demand/supply and present forecasts/state-of-business to investors - so, as a policy 10% of all IT resources are ear-marked for 'analytics'
* And as a policy, no more than 5% of IT resources should be consumed by monitoring and management tools - these are overheads and should be kept to a minimum after all 

#### IT Resources
Now, how much IT resources will WebTraveller require? Since I am the de facto CIO of WebTraveller and it is my first CIO job, I decide to start with a nice whole number - say 100 servers (okay, I hear you, all on cloud). Now, here is the split up of what this 100 servers are going to do…
  
<table class="table table-bordered table-striped table-condensed bs-docs-grid">
	<tr>
		<td>1</td>
		<td>Servers running WebTraveller's Ruby based dynamic Web-Application</td>
		<td>25</td>
	</tr>
	<tr>
		<td>2</td>
		<td>Servers sourcing WebTraveller's static page and load-balancer (httpd/nginix)</td>
		<td>15</td>
	</tr>
	<tr>
		<td>3</td>
		<td>Servers running WebTraveller Java interface with its data providers</td>
		<td>25</td>
	</tr>
	<tr>
		<td>4</td>
		<td>WebTraveller database servers</td>
		<td>20</td>
	</tr>
	<tr>
		<td>5</td>
		<td>IT analytics</td>
		<td>10</td>
	</tr>
	<tr>
		<td>6</td>
		<td>IT management/monitoring</td>
		<td>5</td>
	</tr>
	<tr>
		<td>-</td>
		<td>Total Servers</td>
		<td>100</td>
	</tr>
</table>	
	
*(How much am I off the mark in these assumptions? If its horrific, then please let me know and I promise to re-do this blog)*

#### Quantum of data to collect
Being the CIO, I want to understand how my IT is coping. So I need data. Data on server's utilisation, database metrics, web-server metrics etc. Industry calls these various metrics as KPI - Key Performance Indicators. So KPI it will be. How many KPIs do I need to collect for each type of IT resource?

<table class="table table-bordered table-striped table-condensed bs-docs-grid">
	<tr>
		<td>#</td>
		<td>KPI Type</td>
		<td>Approximate Number of KPIs per instance</td>
		<td>Number of Instances (from the above table)</td>
		<td>Total KPIs to collect</td>
	</tr>
	<tr>
		<td>1</td>
		<td>Operating system level KPIs - CPU, RAM, open sockets, HDD usage, network card stats etc</td>
		<td>5</td>
		<td>100</td>
		<td>500</td>
	</tr>
	<tr>
		<td>2</td>
		<td>Ruby web-app KPIs</td>
		<td>10</td>
		<td>25</td>
		<td>250</td>
	</tr>
	<tr>
		<td>3</td>
		<td>Ruby web-app runs on the Rails server. KPIs that speak Rails health</td>
		<td>10</td>
		<td>25</td>
		<td>250</td>
	</tr>
	<tr>
		<td>4</td>
		<td>Java web-app KPIs</td>
		<td>10</td>
		<td>25</td>
		<td>250</td>
	</tr>
	<tr>
		<td>5</td>
		<td>Java web-app's use a JVM and app-server (JBoss/Glassfish/Tomcat). KPIs that speak Java platform health</td>
		<td>10</td>
		<td>25</td>
		<td>250</td>
	</tr>
	<tr>
		<td>6</td>
		<td>HTTPD or NGINIX KPIs</td>
		<td>10</td>
		<td>15</td>
		<td>150</td>
	</tr>
	<tr>
		<td>7</td>
		<td>Database Server KPIs</td>
		<td>20</td>
		<td>20</td>
		<td>400</td>
	</tr>
	<tr>
		<td>8</td>
		<td>KPIs from the Analytics system (say running Hadoop)</td>
		<td>10</td>
		<td>10</td>
		<td>100</td>
	</tr>
	<tr>
		<td>-</td>
		<td>Total</td>
		<td>-</td>
		<td>-</td>
		<td>2150</td>
	</tr>
</table>

So, the approximate total number of KPIs to collect is 2150. Which is an average of about 21 KPIs to be collected from the 100 servers of WebTraveller. Now, how frequently do we want to collect this data? I as the CIO of WebTraveller want my IT to be really AGILE - which means I don't want to miss any data (especially in its initial days!). And I also want to keep it SIMPLE. So I ask my monitoring team to collect all these KPIs *every minute*.  

#### The Developer's View
'Mr. Bean' is a developer in WebTraveller's IT team. Mr. Bean's task is cut out - he has to develop the monitoring app that collects 2150 metrics every minute by polling. Being a seasoned developer, he knows for sure that to collect so many KPIs he needs to code a 'multi-threaded' application. So Bean decides to do some estimation. How many threads will his application need to capture 2150 KPIs every minute?

First of all, what are the different methods that exist to capture these KPIs from a remote server? Here are the necessary few -

* JMX to collect from Java applications
* JDBC to collect from the databases itself
* RPC/RMI or SSH based log-monitoring mechanism to retrieve data from the Ruby part
* RPC/RMI or SSH based log-monitoring to retrieve data from HTTPD/NGINX
* Server level stats through remote SSH

Mr. Bean calculates the response-time for various collection methods - 

<table class="table table-bordered table-striped table-condensed bs-docs-grid">
	<tr>
		<td>#</td>
		<td>Collection Method</td>
		<td>Observation</td>
		<td>Mean time to collect a set of KPIs from one instance</td>
		<td>Num of servers that can be covered in 1 minute in a single thread</td>
	</tr>
	<tr>
		<td>1</td>
		<td>SSH</td>
		<td>SSH involves two types of time - (1) time taken for connection establishment and teardown (2) multiple commands need to be run on the remote shell, data collated and retrieved</td>
		<td>15 seconds</td>
		<td>60/15 => 4 servers</td>
	</tr>
	<tr>
		<td>2</td>
		<td>JMX</td>
		<td>Multiple JMX attributes can retrieved at once. But here again there are the 2 phases of connection and retrieval</td>
		<td>15 seconds</td>
		<td>60/15 => 4 servers</td>
	</tr>
	<tr>
		<td>3</td>
		<td>JDBC</td>
		<td>Single JDBC session can get a lot of metrics</td>
		<td>Assume 20 seconds to retrieve all 20 database server KPIs of one instance</td>
		<td>60/20 => 3 servers</td>
	</tr>
	<tr>
		<td>4</td>
		<td>RPC or RMI</td>
		<td>Am not sure if multiple variable can be retrieved in a single session. Assuming its possible...</td>
		<td>15 seconds</td>
		<td>60/15 => 4 servers</td>
	</tr>
</table>

With this understanding, Mr. Bean decides on which collection technology to use for each class of KPIs (in table below). Also, Mr. Bean wants to know the number of threads his application may have to run. Mr. Bean knows that ideally, he would want to do Asynchronous collection for each of these - that is, start a request in Thread-A and retrieve the data from Thread-B when it arrives - there are many libraries that provide such Asynchronous capabilities for each of SSH, RPC, RMI, JMX, JDBC etc. However, Asynchronous communication does not lead to conservative number of threads - a thread gets forked whenever data arrives. For most conservative number of threads, a select-and-poll based method is most appropriate. The big deficiency of select-and-poll approach however is that data collection with time boundaries becomes tougher. There is no guarantee that the above mentioned mean times will always hold good. And also, the data that arrives is distributed wildly on the temporal scale. 

So, Mr. Bean calculates the number of threads that his application will end-up with if he takes either of the approaches -

<table class="table table-bordered table-striped table-condensed bs-docs-grid">
	<tr>
		<td>#</td>
		<td>KPI Type</td>
		<td>Data Collection Technology</td>
		<td>Number of Instances (from the above table)</td>
		<td>Number of threads for select-and-poll approach</td>
		<td>Number of threads for asynchronous approach</td>
	</tr>
	<tr>
		<td>1</td>
		<td>Operating system level KPIs - CPU, RAM, open sockets, HDD usage, network card stats etc</td>
		<td>SSH</td>
		<td>100</td>
		<td>(100 servers/4 servers per min per thread) => 25 threads</td>
		<td>100</td>
	</tr>
	<tr>
		<td>2</td>
		<td>Ruby web-app KPIs</td>
		<td>RPC or RMI</td>
		<td>25</td>
		<td>(25/4) => 6.25 (assuming fractions in num threads is possible!)</td>
		<td>25</td>
	</tr>
	<tr>
		<td>3</td>
		<td>Ruby web-app runs on the Rails server. KPIs that speak Rails health</td>
		<td>RPC or RMI</td>
		<td>25</td>
		<td>(25/4) => 6.25</td>
		<td>25</td>
	</tr>
	<tr>
		<td>4</td>
		<td>Java web-app KPIs</td>
		<td>JMX</td>
		<td>25</td>
		<td>(25/4) => 6.25</td>
		<td>25</td>
	</tr>
	<tr>
		<td>5</td>
		<td>Java web-app's use a JVM and app-server (JBoss/Glassfish/Tomcat). KPIs that speak Java platform health</td>
		<td>JMX</td>
		<td>25</td>
		<td>(25/4) => 6.25</td>
		<td>25</td>		
	</tr>
	<tr>
		<td>6</td>
		<td>HTTPD or NGINIX KPIs</td>
		<td>SSH</td>
		<td>15</td>
		<td>(15/4) => 4</td>
		<td>15</td>
	</tr>
	<tr>
		<td>7</td>
		<td>Database Server KPIs</td>
		<td>JDBC</td>
		<td>20</td>
		<td>(20/3) => 7</td>
		<td>20</td>
	</tr>
	<tr>
		<td>8</td>
		<td>KPIs from the Analytics system (say running Hadoop)</td>
		<td>SSH</td>
		<td>10</td>
		<td>(10/4) => 3</td>
		<td>10</td>
	</tr>
	<tr>
		<td></td>
		<td>Total</td>
		<td>-</td>
		<td>-</td>
		<td>64 threads</td>
		<td>245 threads</td>
	</tr>
</table>

So the realm of number of threads to gather information from the 100 server deployment at WebTraveller is approximately between 60 to 250 threads. The following factors are pertinent - 

* Usage of async libraries would provide a better temporal distribution and fault-safety 
* With a select-and-poll approach, the 64 threads will be active all the time. With Asynchronous approach 245 threads are forked every minute and they end much before the minute boundary (hopefully)
* The number of socket descriptors required will have one-to-one correspondence with number of threads (in this case). So 64 sockets will be open at any point of time by the polling approach, while up to 245 sockets could be open at any point of time by the asynchronous approach
* One can always mix-and-match between polling and asynchronous data collection for different types. For example JMX can be collected in an asynchronous way while SSH can be collected by the polling methods

#### Is there a Data Collection 'Challenge'? 
The numbers say that on average 1 to 2 threads/sockets are required to collect data from each instance. This does not sound much in WebTraveller's case but one needs to pay attention to the following details - 

* I have assumed that all data points are to be collected per minute. It could very well be that data is required at a much more granular level for certain metrics - say every 5 seconds. In which case, the number of threads and sockets would simply go up 12 times!
* The average number of KPI per server, at 21, is a super conservative estimate. In most production environments, this number will at least double and generally, much much higher 
* I have not considered the challenge (if there is one) on the persistence side of things - how easy is to to store all this data in a RDBMS (or NoSql!) and design queries for real-time?
* And these numbers need to be coupled with the natural challenges of data collection, which are - 
	* Horizontal scalability
	* Fault tolerance
	* More accurate temporal distribution
	* Tired architecture induces delay in real-time collection and storage

#### With little more scale
The situation changes considerably if we consider a data-center with 3000 servers. Even with a linear extrapolation, it would involve collection of about 65,000 data-points every minute. And in excess of 10,000 threads and sockets. 

#### With Ganglia
In WebTraveller's case, Mr. Bean could potentially do one other thing. He could use Ganglia to collect the data. Each of the 5 functional groups (from the first table above) in WebTraveller's data-center could be configured as separate Ganglia clusters. This leads to -

* The data collector having to communicate with only 5 servers instead of 100 - because Ganglia stores the data collected in each cluster at all the nodes
* In each cluster, Ganglia collects data for the constituent supporting collection technology (JMX/JDBC etc). Ganglia can use UDP Multicast, thereby only 5 threads are required to collect ALL the data. Well, now, thats one huge optimisation, isn't it? On the aside however, the Ganglia clients will have to be extended to collect from diverse sources and pre-installed on all servers. Yet, the huge saving in monitoring cost is visible in a straightforward way... 
* Total linear scalability - even if the cluster sizes go up ten times, the load on the management server does not increase at all. The payload from each cluster might go up - but that is not much cost in collection
* Low granularity polling - with Ganglia, very low granularity polling within each cluster does not increase the load on the monitoring server. The monitoring server can continue to receive data on minute boundaries after all
* The positive effects of local storage and fault tolerance that a Ganglia based monitoring can provide

