{{{
    "title": "Few days with Apache Cassandra",
    "tags" : [ "cassandra", "java", "database" ],
    "category" : "technology",
    "date" : "07-11-2013",
    "description" : ""
}}}

Few years ago I was a product developer at a big software (but non-database) company. We were writing the v2 of a new product after a fairly successful development round of v1. For everything OLTP, we used the wonderful open-source database - Postgres. But by v2, we had new, hight-volume data like NetFlow coming in. This would have intensely tested Postgres's scalability and read/write performance. And we had some datawarehousing and OLAP requirements too. A hard look at our queries told us that column-stores would be a great-fit. Looking back, the options for a new product to store and query on massive data volumes boiled down to these few options -

* Throw more hardware: Tell the needy customer to invest more in hardware. But no one really knew how much more hardware was really going to nail it
* Tune, Shard, Rebuild, Redeploy: Invest in tuning our software and database for specific queries. Shard, re-model and/or do whatever that could be done by the development and implementation teams around what we had
* Use Oracle
	* This did not make good business sense for a big product company - tying itself deep into Oracle
	* CTO and architects did not think Oracle could nail the data volumes anyway (actually none of the engineers who understood the problem thought Oracle would nail it anyway!)
* Use column-stores like Sybase, Vertica

The fact was, there were no open-source, reliable, horizontally scalable column-stores or parallel DBMS to consider.

Times have improved. We now have Cassandra, HBase, Hypertable etc (MongoDB, CouchDB etc are document stores with less of modeling - here the context is of schema-full data with rich data-type support).

So, I decided to try and understand Cassandra. Wanted to answer the simple question - if I were to re-live the product development scenario described above, would I choose Cassandra? So in this article I talk about my experiment with Cassandra. Here, I choose a very specific use-case to illustrate what I found - Monitoring JVM metrics in a small data center.

#### A Simple Usecase
* A web company running 50 JVMs. The JVMs could be Apache-Tomcat servlet containers hosting the application
* Each Tomcat instance hosts 50 URLs and thereby, say, 50 front-ending servlet classes each extending HttpServlet
* Method metrics are collected on these servlets (through logs or bytecode instrumentation or aspect-driven). Specifically, the metrics collected - number of invocations and time-spent - just 2 method level metrics!
* Idea is to analyze the metrics to get insights into - how to deploy the servets servers? Are there any hotspots and, if so, where - which URL (object) is being accessed most/least? at what times? trends? and so on…
* Along with monitoring these specific servlet method's also keep a tab on overall application health. The number of active-threads in all JVM's. Various JVM memory parameters. A few MBean stat's. Etc…
* Minimum data view granularity requirements -
	* Last 30 days  - per-minute, per-hour, per-day, per-week, per-month
	* Last 60 days  - per-hour, per-day, per-week, per-month
	* Last 180 days - per-day, per-week, per-month
	* Last 360 days - per-week (52 weeks), per-month
	* Last 720 days - per-month (24 months)
* User primarily requires 'trend' and 'topN' charts. Examples -
	* Chart of Top-10 most invoked servlets in last 2 months at per-hour granularity
	* Trend of three specific servlet's response-times {max, min, avg, 1st and 3rd quartile} over last 6 months plotted per day
* User also wants JVM wide statistics like - active threads, memory stats and datasource stats - all following the same granularities as above. Lets suppose that these combine to 6 separate metrics in all. 
*  From the querying perspective, lets say we have only 2 users in our IT Operations team who will be actively querying this data.
	
#### Data Volumes

###### Fine-grained Data

* JVM Method data: 
	* 50 JVMs * 50 Methods * 24 Hours in a day * 60 minutes per hour * 2 metric-types = 7.2 Million data-points per day. 
	* 7.2 Million * 30 = 216 Million data points per month
* JVM-wide stats: 
	* 50 JVMs * 24 Hours * 60 minutes * 6 metric-types = 432K data points per day 
	* 432K * 30 = 12.96 Million per month

###### Coarse-grained Data

* This corresponds to roll-ups. Hourly, Daily, Weekly and Monthly.
* Hourly rollup for last 60 days
	* JVM method data: 50 JVMs * 50 Methods * 24 Hours * 60 days * 2 metric-types = 7.2 Million data points over last 60 days. Or, 120K data points per day
	* JVM-wide stats: 50 JVMs * 24 Hours * 60 days * 6 metric-types = 432K data points over last 60 days. Or, 7.2K data points per day
* Daily rollup for last 180 days
	* JVM Method data: 50 JVMs * 50 Methods * 180 days * 2 metric-types = 900K data points in 180 days. Or, 5K data points per day
	* JVM-wide stats: 50 JVMs * 180 days * 6 metric-types = 54K data points in 180 days. Or, 300 data points per day
* Weekly rollup for last 52 weeks
	* JVM Method data: 50 JVMs * 50 Methods * 52 weeks * 2 metric-types = 260K data points over last 52 weeks. Or, 5K data points per week. Or, 700 data points per day
	* JVM-wide stats: 50 JVMs * 52 weeks * 6 metric-types = 15.6K data points over last 52 weeks. Or, 300 data points per week. Or, 40 data points per day
* Monthly rollup for last 24 months
	* JVM Method data: 50 JVMs * 50 Methods * 24 months * 2 metric-types = 120K data points for last 24 months. Or, 5K data points per month. Or, 170 data points per day
	* JVM-wide stats: 50 JVMs * 30 days * 6 metric-types = 9000 data points per month. Or, 300 data points per month. Or 10 data points per day

###### Adding it all up!

Number of data points collected PER DAY -

* JVM Method data:
	* Fine grained minute data points = 7.2 Million
	* Hourly rollup = 120K
	* Daily rollup = 5K
	* Weekly rollup = 700
	* Monthly rollup = 170
	* Total (approx) = 7.32 Million
* JVM-wide stats:
	* Fine grained minute data points = 432K
	* Hourly rollup = 7.2K
	* Daily rollup = 300
	* Weekly rollup = 40
	* Monthly rollup = 10
	* Total (approx) = 440K
* Total of totals = 7.76 Million data points per day. Or, 320K data points per hour. Or, 5500 data points per minute. Or 90 data-points per second

There are couple of VERY IMPORTANT things to realize before going further -

* In the DBMS world, multiple data points can fit into a single row. So, 90 data-points per second translates to fewer than 90 row inserts per second. But how fewer depends on the data modeling
* The temporal distribution of inserts is not even. The hourly roll-up kicks in at the end of each hour. Daily roll-up at the end-of-day and so on (not considering the timezone adjustments required for roll-ups)


Small-data problem? Its just a prototype!!


#### Before we start data modeling... 
######Data Access methods in Cassandra
Predominantly, there are three ways to interact with Cassandra - Hector, Astyanax and CQL. Cassandra supports Thrift by providing an API. Hector and Astyanax use the Thrift API to talk to the DBMS. CQL3 proposes a new SQL like API. This [slidedeck](http://www.slideshare.net/jericevans/cql-sql-in-cassandra) has CQL3 performance vis-a-vis Thrift-API by the main committer of this piece - Eric Evans. Take your pick! In this prototype, I use CQL3. 

######SuperColumns
Recent articles and blogs suggest that supercolumns are a bad design and will go away in future releases of Cassandra. So I use composite keys and not supercolumns to model the data

######Denormalization and Data Modeling by Queries
One of the central ideas in column-stores is to model data per the queries expected. Also denormalize, that is, store multiple replicas of data if required. Both these ideas have strong theoratical backing. Let me state just two -

* DB schema per query requirements - One of the gurus of database design, Professor Stonebraker has suggested that in enterprise applications OLTP queries are well known in advance, few in number, and do not change often. Refer to [this paper](http://cs-www.cs.yale.edu/homes/dna/papers/vldb07hstore.pdf).
* Denormalization - RDBMS belongs to the era when storage was expensive. Its not so anymore. CPUs are far more expensive (in both ways - CapEx and OpEx ). And DB queries take CPU cycles. And a waiting user could have tangible/intangile revenue implications of web companies. All put together, model database sparsely and denormalized. Store multiple versions and replicas of data. Do anything to make queries faster!

######Code Itself 
The JBoss7 based implementation of this prototype can be found in my github [repository](https://github.com/bharath12345/JvmDataStorage). You will find a couple of MBean's - JvmMethodMetricsDAO and JvmMethodIdNameDAO which have the persist() and find() methods. The procedure to use this is -

1. Build the artifact using maven - 'mvn clean install' at the top level directory
2. Deploy the jim-ear.ear in JBoss's standalone/deployments
3. Start JBoss's jconsole and you should be able to see these MBean's in the jconsole's UI

#### Data Modeling
Here are few of the broad guidelines I set and followed -

* One Keyspace each for both types of data (JVM methods and JVM-wide stats). Each keyspace holds raw (fine grained) and roll-up data
* As few strings as possible in the stores
* Keep row-key and columm-key string names small
* Many data items like JVM_ID will need a mapping table to map JVM-Name to a UUID
* Row Key -
	* For fine grained, minutely data, row key is a combination of JVM_ID and date (20130628 for 28th June 2013)
	* All roll-up tables have JVM_ID as the row key
* Columns for roll-up data
	* Hourly  Roll-up: 60 days,  2 months  => 24 * 60 = 1440 columns
	* Daily   Roll-up: 180 days, 6 months  => 180 columns
	* Weekly  Roll-up: 350 days, 50 weeks  => 50  columns
	* Monthly Roll-up: 720 days, 24 months => 24  columns
* Cassandra has this superb concept of tombstones and data cleanup. This can be triggered by setting a TTL field during inserts. TTL is set in seconds and I used the following setting in this prototype -
	* Raw:             30  days => 30 * 24 * 60 * 60  => 2,592,000
	* Hourly  Roll-up: 60  days => 2 * 2,592,000      => 5,184,000
	* Daily   Roll-up: 180 days => 3 * 5,184,000      => 15,552,000
	* Weekly  Roll-up: 350 days => 350 * 24 * 60 * 60 => 30,240,000
	* Monthly Roll-up: 720 days => 4 * 15,552,000     => 62,208,000

##### Keyspace Configuration
###### For JVM Method metrics

    CREATE KEYSPACE JvmMethodMetrics    WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};

###### For JVM wide statistics
    
    CREATE KEYSPACE JvmMetrics          WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};

##### Column Families in JvmMethodMetrics KEYSPACE
###### Raw Trend Query Tables
    CREATE TABLE JvmMethodIdNameMap (
        jvm_id int,
        method_id int,
        method_name varchar,
        PRIMARY KEY (jvm_id)
    );

    CREATE INDEX jvm_method_name ON JvmMethodIdNameMap (method_name);

    CREATE TABLE JvmMethodMetricsRaw (
        jvm_id int,
        date varchar,
        day_time int,
        method_id int,
        invocations bigint,
        response_time float,
        PRIMARY KEY (jvm_id, date)
    );

    CREATE INDEX jvm_method_id ON JvmMethodMetricsRaw (method_id);

###### Trend Query Roll-up Tables
    CREATE TABLE JvmMethodMetricsHourly (
        jvm_id int,
        hour int,
        method_id bigint,
        invocations bigint,
        response_time float,
        PRIMARY KEY (jvm_id)
    );

    CREATE TABLE JvmMethodMetricsDaily (
        jvm_id int,
        day int,
        method_id bigint,
        invocations bigint,
        response_time float,
        PRIMARY KEY (jvm_id)
    );

    CREATE TABLE JvmMethodMetricsWeekly (
        jvm_id int,
        week int,
        method_id bigint,
        invocations bigint,
        response_time float,
        PRIMARY KEY (jvm_id)
    );

    CREATE TABLE JvmMethodMetricsMonthly (
        jvm_id int,
        month int,
        method_id bigint,
        invocations bigint,
        response_time float,
        PRIMARY KEY (jvm_id)
    );

###### TopN Query Tables
Data in these tables is kept sorted by maximum (response-time/invocations) to minimum
    
    CREATE TABLE JvmMethodTopNHourly (
        jvm_id int,
        hour int,
        method_id_type varchar,      // Example: 100_RT => for method 100 response-time, 103_INV => for method 103 invocation count
        response_time_map map<text, float>,
        invocation_count_map map<text, long>,
        PRIMARY KEY (jvm_id, hour)
    );

    CREATE TABLE JvmMethodTopNDaily (
        jvm_id int,
        day int,
        method_id_type varchar,
        response_time_map map<text, float>,
        invocation_count_map map<text, long>,
        PRIMARY KEY (jvm_id, hour)
    );

    CREATE TABLE JvmMethodTopNWeekly (
        jvm_id int,
        week int,
        method_id_type varchar,
        response_time_map map<text, float>,
        invocation_count_map map<text, long>,
        PRIMARY KEY (jvm_id, hour)
    );

    CREATE TABLE JvmMethodTopNMonthly (
        jvm_id int,
        month int,
        method_id_type varchar,
        response_time_map map<text, float>,
        invocation_count_map map<text, long>,
        PRIMARY KEY (jvm_id, hour)
    );

##### Column Families in JvmMetricsRaw KEYSPACE

    CREATE TABLE JvmMetricsRaw (
      jvm_id int,
      date varchar,
      day_time int,          
      total_live_threads int,
      
      mem_heap set<bigint>, 			// 3 data points - commited, max, used
      mem_nonheap set<bigint>,
      		
      ds_freepool map<int, bigint>,	// key is datasource_id, free pool of
	  ds_usetime map<int, bigint>		// threads, avg query time over 1 min
	  		
      PRIMARY KEY (jvm_id, date)
    );

#### Query Code
CQL3 packs a [QueryBuilder](http://www.datastax.com/documentation/developer/java-driver/1.0/index.html#java-driver/reference/queryBuilder_r.html) utility that offers some basic features. Refer to the QueryBuild JavaDocs for more info. I was able to build simple queries for 'select' using different 'where' clauses for time and ID's without much effort. I would recommend users to extend Cassandra's QueryBuilder in their DAO layer to provide model specific functionality and catch errors. The prototype offers a Entity/DAO model which can be easily understood by those familiar with JPA/Hibernate. (However I am not a fan of the many ORM frameworks that are coming up for Cassandra - the knowledge of 'entity' modeling is critical for performance problems which Cassandra proposes to handle. Using a Cassandra ORM framework would mean lesser knowlege of data model and consequently less performant queries. Stay away from them!)

#### Read/Write Performance
Post modeling and unit testing I ran the application on my laptop (MacBookPro 2.9GHz/8GB RAM). Since my laptop is not an ideal performance test environment (I have multiple applications running, no tuning of cassandra or JBoss) I see no point in publishing the numbers or charts. However, I was able to 'write' literally millions of records per minute and read them back. Since I run MySql as well on my laptop, one thing I can vouch for is that Cassandra's write performance is definitely far ahead of what I would have expected from my OOTB MySql. 

#### Conclusion
Cassandra has come a long way from the 0.8 days. I did not come across any bugs working on my prototype. CQL3 and data modeling was a breeze. And there are a plethora of resources on this topic on the web. I would certainly recommend Cassandra for those looking to get a quick hang of NoSql and Column stores. If you are planning to use Cassandra as part of your application and have done the due deligence on the performance side, then, let me assure you - programming with Cassandra should not take any more time than using a ORM framework like JPA/Hibernate. And if you are like me, wanting to write a prototype then you should be able to wrap it all up from zero to running in a single working week. Ping me if you run into any issues using my code, understanding my blog or anything else. Thanks for reading!

#### Reading Recommendations
* Good introduction on the subject - [O'Reilly's Cassandra Definitive Guide](http://shop.oreilly.com/product/0636920010852.do), 
* Data Modeling - [this](http://www.ebaytechblog.com/2012/07/16/cassandra-data-modeling-best-practices-part-1/) wonderful blog by Jay Patel from Ebay
* Performance comparisons - [this](http://www.datastax.com/dev/blog/2012-in-review-performance) article really nails it (pay attention to the chart!)

