{{{
    "title": "My Scala Projects In The Making",
    "subheading": "",
    "tags" : [ "" ],
    "category" : "technology",
    "date" : "25-12-2013",
    "description" : "",
    "toc": true
}}}

Over the last year I often heard my friends say the era of [MOOC](http://en.wikipedia.org/wiki/Massive_open_online_course) was truly upon us. It was only on taking up couple of Coursera courses did I realise it fully. They have been eye-opening many times over (and extremely rigorous). Would particularly recommend these two to anyone wanting to understand programming for the multicore, realtime, big-data world -

* [Functional Programming Principles In Scala](https://class.coursera.org/progfun-003)
*  [Principles Of Reactive Programming](https://class.coursera.org/reactive-001)

I have been trudging on and off with Scala for the latter half of 2013. Written many small programs to understand the core concepts. But doing these two courses have put me on very firm footing. The courses had me working on 12 solid assignments. And not a single one of these took me less than couple of days. The assignments cover a lot of ground which includes -

* Composing non-trivial higher order functions
* Mixing object oriented with functional
* Usage of Scala collections along with language built-in's like pattern-matching
* Testing with ScalaTest and ScalaCheck
* Using RxJava and Observables on non-trivial data-set
* Using Akka for Actor based concurrency

Inspired by these assignments, I have been working on few of my own ideas. Specifically, three projects (all of which are still in their infancy). However as I head out with family for a vacation to usher in the new year, I thought of writing this as a post-it on my web-wall. One of the new year resolutions is to invest more time and energy into these projects.

<hr>

### GBridge

* **Project Goal**: Data bridge between [Ganglia](http://ganglia.info/) (gmond) and [ZeroMQ](http://zeromq.org/)
* **The Why**
  * *Why Ganglia?* Because it is (probably) the worlds most popular open-source data collection tool for large data centres
  * *Why ZeroMQ?* Because it is (probably) the worlds most popular open-source data-bus for high volumes, with API in most programming languages
* **Specifics**
  * Ganglia's *gmond* agent responds with cluster wide metric health on TCP in XML. GBridge polls this data
  * GBridge can collect data from multiple clusters and *any* or *random* host within the cluster
  * GBridge is optimised for minimum polling of *gmond*
  * Each metric is published only once (and as a separate message) per polling cycle on ZeroMQ
  * Each metric is published as JSON
  * Use actor based concurrency and futures for polling multiple gmond nodes, parsing response and publishing on ZeroMQ
  * Completely in Scala
  * Graceful degradation on load. Support distribution, automatic recovery on errors and failover
  * Going ahead support [Collectd](http://collectd.org/) on data ingress side. Support writing to [OpenTSDB](http://opentsdb.net/) on the data egress side
* **Code Status**
  * [Coded](https://github.com/bharath12345/gBridge) the data collection, parsing and publish to ZeroMQ
  * Tested only for small loads
  * Very little unit test code
  * Yet to design for distribution, recovery and failover
  
<hr>

### ScaLog
* **Project Goal**: [Jekyll](http://jekyllrb.com/) or [PoetJS](http://jsantell.github.io/poet/) like markdown based static blogger in Scala
* **The Why**
  * Scala lends better for server side coding. Learn and implement a full-stack web application in Scala
  * For larger blogs, features like full text search can be much faster in Scala than Ruby or JavaScript
  * Apart from human-readable HTML interface, also provide a machine-readable   RESTful interface
  * Option to store the markdown in flat files on the server side or source it from a RDBMS (PostgreSQL)
* **Specifics**
  * ScaLog uses [Spray](http://spray.io/) for HTTP Server side (for both RESTful interface and HTML pages)
  * ScaLog uses [pegdown](https://github.com/sirthias/pegdown) for markdown processing 
  * ScaLog uses [Slick](http://slick.typesafe.com/) to read and write to RDBMS from Scala (ORM like)
  * Cloud platforms for applications like Heroku are the main target for deployment 
* **Code Status**
  * The [code](https://github.com/bharath12345/myspray) for CRUD (post/get/put/delete) operations for the blog with RESTful URLs is complete up to proof-of-concept
  * The code for CRUD at the database layer also done
  * pegdown parsing of markdown complete
  * Work needed to easily extend the URLs, support UI templating and much more

<hr>

### WebFlow
* **Project Goal**: [NetFlow](http://en.wikipedia.org/wiki/NetFlow) like UDP export of ingress-egress data at Web-Servers 
* **The Why**
  * Gone are the [C10K](http://www.kegel.com/c10k.html) problems. We are now in the world of [C10M](http://c10m.robertgraham.com/p/manifesto.html) and beyond. With such high volume of connections, to account for all the request-responses hitting the web-servers, it is not sufficient to use polling based (JMX like) or log based mechanisms. Dictionary export mechanisms are valid contenders when the volumes are so large
  * All the good reasons of why Netflow/Sflow are wonderful methods for volume accounting (at high volumes) at switch/router level 
* **Specifics**
  * Plug-in for Jetty/Netty/Spray/Servlet containers
  * Completely Scala. Akka actor based
* **Code Status**
  * Design done. Yet to start coding