{{{
    "title": "Application Developer View on PostgreSQL vs. MySQL",
    "subheading": "",
    "tags" : [ "" ],
    "category" : "technology",
    "date" : "1-16-2014",
    "description" : "",
    "toc": true
}}}

I reluctantly started to write this post some 6 months ago. As a application developer my knowledge of the internals of DBMS design was (and still is) very limited. It is one thing to work with a DBMS at *development* and quite another to keep it running as part of *IT Operations*. My motivation here is to share a few specific ideas with fellow application developers. The attempt is to do a value judgement of the two systems from a development standpoint and steer clear from a value judgement in the *deployed* scenario. After all DBMS systems are probably at the heart of more Aps vs. Ops debates than anything else.

Now, to say it simply (at the cost of barbs from some of my good friends who I know to be excellent operations engineers for MySQL) - **PostgreSQL leads MySQL. And by some distance.**

Apart from reading about the internals and playing with both systems I felt a need speak to whomever I could in the developer community to ask for the reasons behind the choice of DBMS in their projects. In the last 6 months I could speak to just about eight such people in different projects. Almost all from medium to small companies doing web applications (but some of these projects were themselves quite large). After speaking to these people there is one thing that I cannot but share - the answer to *Why MySQL* from all who had chosen it was - *"Unfortunately, MySQL had already been chosen by the time I got involved"*. Of the eight, six had been running projects for 2-3 years of which three had chosen MySQL. Rest had all opted for PostgreSQL.

When I told a colleague of writing this article he smiled and asked a polite, *Why?* After all, the web is filled with such articles. Mostly written by expert database admins. There are fewer articles from the *application programmer* point-of-view. I can think of two reasons why there are not many programmers dissecting this -

1. *Developers* find it difficult to talk on this topic in which the *Operations* folk have strong opinions. In many projects of the DevOps kind the decision to pick the database is the prerogative of the *Operations* folk than the *Developer* folk
2. From a developer perspective, the PostgreSQL vs. MySQL debate is a non-starter. PostgreSQL wins. And wins quite early (you will know the *why* by the end of this post)  

But before delving deeper into the comparison its good to set the application context -

1. Enterprise Applications. By this, I mean the application has more moving parts than a typical web-stack. The number of tables could stretch into hundreds. Data is collected from myriad sources in real-time
2. Read-write ratio varies vastly across tables. Database needs to support 90% (and upwards) read-only tables and also tables with much higher write than read, say 60% (and upwards)
3. Many thousand transactions per second
4. Hundreds of stored procedures
5. Automating migrations, upgrades and sharding

Given that the topic is vast and both softwares are widely used its probably a good idea to start by pointing to some of the good references for comparison from the wild web -

1. [MySQL vs. PostgreSQL](http://www.wikivs.com/wiki/MySQL_vs_PostgreSQL) - recent and continuously updated. Readers would do well to read the articles in the links section (on last read, I did not find a single article talking glowingly about MySQL in comparison to PostgreSQL) 
2. Couple of very good articles comparing these two by Robert Haas
   * [Table Organization](http://rhaas.blogspot.in/2010/11/MySQL-vs-postgresql-part-1-table.html)
   * [Vacuum vs. Purge](http://rhaas.blogspot.in/2011/02/mysql-vs-postgresql-part-2-vacuum-vs.html)
3. [PostgreSQL vs MySQL: Which is better?](http://www.databasejournal.com/features/postgresql/article.php/3288951/PostgreSQL-vs-MySQL-Which-is-better.htm) - This article is 10 years old. Still a good read
4. [MySQL Gotchas](http://sql-info.de/MySQL/gotchas.html) and [PostgreSQL Gotchas](http://sql-info.de/postgresql/postgres-gotchas.html). Just stare at the size of these two lists for some time even if you don't read them. They tell a story
5. [Comparing Reliability and Speed](http://wiki.postgresql.org/wiki/Why_PostgreSQL_Instead_of_MySQL:_Comparing_Reliability_and_Speed_in_2007)
6. [A Comparison of Enterprise Suitability - PostgreSQL is Suited Better](http://www.slideshare.net/techdude/postgres-vs-MySQL-presentation) - though MyISAM focused, this comparison is with enterprise products in purview and is 5 years old (2008). Since then, the gap between PostgreSQL and MySQL have only widened in favour of PostgreSQL despite InnoDB

I plan and hope not to repeat anything that is already said in these articles. And agreeing with the many writers of these articles, I don't see any point doing performance benchmark comparisons between these two database systems. But I do want to point the interested readers to the *[political](http://www.muktware.com/2013/05/there-is-no-reason-at-all-to-use-MySQL-mariadb-MySQL-founder-michael-widenius/4298)* aspects in this comparison (I have quoted from this interview at the end of this article). MySQL has been acquired by Oracle. Its only natural to have concerns about the future roadmap of MySQL given these concerns which affect technology deeply...

Moving on to the specifics...

### The 'Null' Problem
The biggest accusation one can make against any RDBMS is that it is not careful with data integrity. MySQL is notorious for its inability to handle Null with many data types. Effort to accommodate query mistakes ruins MySQL. For example - 

* MySQL will insert empty strings for text fields that have not-null constraint. This happens if you forgot to mention a field during the insert or if you somehow ended up inserting a blank value ('') for a field. It goes ahead with the insert in both these cases. Irrespective of weather we use ORM or direct JDBC or some other kind of wrappers, there simply is no way to gracefully handle this problem. PostgreSQL won't do such a thing
* Non-null timestamps end up getting all zero value dates. If you push a NULL as date, it defaults to current time!
* With decimal numbers, if you are not careful with precision and scale, then, on an insert MySQL will *change the data* to fit the column constraints. Of course its necessary to be careful when playing with data but the problem here is a change in precision (column constraint) should in no way change the data as MySQL does. This kind of problem is just plain horror. Just refer to the MySQL gotchas site to get a clear understanding of this problem. Postgres does not alter data no matter what
* While writing functions, MySQL does not throw graceful exceptions for divide by zero. It just returns a plain NULL all the time!
* In MySQL set a text field length to *X* and insert a string which is *2X* in length... MySQL will just promptly truncate the extra *X*. Now, for gods sake - the length X was a *constraint*. On trying to insert longer length strings, we expect MySQL to throw errors... not play with our data...
* MySQL has no idea about dates. Try inserting 31st Feb and it will promptly comply inserting crap
* MySQL will allow inserting of strings to decimal columns, sometimes storing it as 0 and sometimes as NULL

These problems are by no means all that is there to be said about MySQL's SQL compliance. MySQL takes liberties to not abide by user supplied constraints in many more situations. And this aspects creates massive problems for developers on both *correctness* and *performance* fronts.

### Object Relational Database System!
PostgreSQL calls itself *Object Relational Database System*. This is so because it brings with itself many new ideas that lend very well with the OOPS modelled world (that developers are so used to). And this paradigm fits the enterprise data models and requirements quite well. Let me state three specific features - 

* Logical Partitioning
* Windowing Functions
* Table Inheritance

Each of these features can be quite critical with the ever increasing data that needs to be handled in today's world. It takes some reading to understand each one but it is well worth the effort. On the other side I fail to find any feature that MySQL brings that may be absent from PostgreSQL (think about it - thats a very big assertion I make!). 

To illustrate the point further let me describe one of my favourite features - *table inheritance* with an example. The below statements create tables where the column *name* belongs to the *base* table (shape) and columns like edge, radius belong to the *derived* tables. This model closely resembles how data is modelled in OOPS. Running the above SQL statements, will result in following status in different tables -
    
* shape - 4 records
* square, circle, rectangle tables - 1 record each!  

SQL Statements -

    CREATE TABLE shape ( name varchar(50) );

    CREATE TABLE square    (edge int)     INHERITS (shape);
    CREATE TABLE circle    (radius int)   INHERITS (shape);
    CREATE TABLE rectangle (w int, h int) INHERITS (shape);

    INSERT into shape     (name)         VALUES ('random')
    INSERT into square    (name, edge)   VALUES ('square', 10);
    INSERT into circle    (name, radius) VALUES ('circle', 10);
    INSERT into rectangle (name, w, h)   VALUES ('rectangle', 5, 10);
    
    
Like 'INHERITS' there also is a 'NO INHERITS' to mixin different tables with precision. And more importantly, Postgres uses partitioning under the covers to enable inheritance. So, not only does inheritance give the programmer flexibility in data modelling lending but it also leads to lesser duplication, and thus helps improve performance! Without inheritance, the engineers will be forced to do multiple table joins and filters (many times going up to boolean value *marker* columns) - which sounds over-engineering for a OOP developer standpoint. Thinking about it, the non-object oriented SQL design adds to overhead to SQL optimiser, makes indexing overhead higher and many more such misses.

### Choice Of Data Types and Storage
MySQL has far fewer data types than PostgreSQL. Adding new data types to MySQL is a non-trivial error-prone work even for experience professionals. Compared to this, PostgreSQL offers a proverbial goldmine of data-types for designers to choose from. Here are some aspects about data-types that really makes PostgreSQL standout vis-a-vis MySQL -

* Data types for Dates - A massive choice to choose from for specific usecases
* Data types for IPv4, IPv6, MAC, Inet address
* Data types for Arrays, JSON, UUID, XML with features like search within Arrays using indexes and where clauses
* Data types for floating point numerics - rounding errors can be eliminated to a much larger extent with the massive choice available in this area 
* Infinity, -Infinity, NaN as values for numeric data types - in MySQL one has no way of modelling these. Modelling these as nulls often leads to programming complexity and errors
* ORM tools often convert 'String' datatype to nvarchar(max) which kills performance on MySQL. Inserting multibyte characters (say Japanese) into varchar fields completely corrupts data (no database exception thrown!). Sometimes it is not sufficient to just change the column type to nvarchar when trying to store multibyte characters. Even the insert statements need a prefix (application level code change if you are using JDBC). PostgreSQL uses default UTF8 encoding. There is no varchar/nvarchar problems. Everything simply works!
* Adding constraints to complex types likes dates is made extremely simple with embedded functions. No such thing possible in MySQL. Special keywords like 'today', 'tomorrow', 'yesterday', 'allballs' etc lend readability to the code
* All strings are default UTF-8 encoded
* Serial and other sequences - leads to very fast ID key finding and incrementing
* Data type for Money!
* Index even functions (no other DB does this)
* Automatic Data Compress by Default

Why are data-types important? Modelling precisely leads to less data stored. When performance becomes important to squeeze out the max performance requires optimised storage... because finally, things in DB schema are going to end up in RAM caches and larger datatypes will mean more space being taken up on the RAM. Less conservatively used RAM cache will bring down the performance of the application more than anything else. 

### Performance
Comparing performance of PostgreSQL and MySQL (InnoDB) is a loaded question. The references I have spelt out earlier have links to many scholarly articles that articulate the subtle differences in the MVCC implementation of both. Both provide row locking, page locking, along with read/write lock separation. After digging into the details picking one of these two on the basis of *performance* comes back to the nature of the application that is being built. Designers should pay attention to three critical questions and answer them sufficiently before making a choice -

* Read/Write characteristics of the application
* Concurrent access characteristics of various tables
* Cost of dirty reads, non-repeatable reads, phantom reads etc

These are not easy questions to answer. The performance area is complex enough and if concurrent writes requirements of an application are extreme then moving away totally from SQL to NoSQL is a better option than trying to split hairs over RDBMS engines. A move to NoSQL brings massive freedom to design around write and concurrent access problems (along with massive responsibility to handle things correctly!). So, choosing MySQL over PostgreSQL due to some notions of higher performance without concrete answers to the above posers, in all probability, will lead to a disaster-in-waiting.

### Philosophical difference that influences technology
Some experts have pointed out a subtle but important philosophical difference between MySQL and PostgreSQL that impacts their core technological offering. MySQL is a *product* while PostgreSQL is a *project*. MySQL has been a product since its inception and sold multiple times over by different companies that have owned it. Due to the *product* definition and ownership, large scale code corrections have been fewer with MySQL. This philosophical difference is what is behind the fact that MySQL is still in v5.x while PostgreSQL in v9.x. This difference also leads to a design where MySQL separates the storage engine and SQL parsing as different (and many different storage engines can be chosen). While PostgreSQL integrates the whole stack top-to-bottom. The folks behind PostgreSQL are driven to bring the progress in database technology to the fingertips of developers and admins. Thats why PostgreSQL has made larger course corrections in its evolutions (lending to a bigger version number v9).

### Epilogue
I have a hypothesis. MySQL is more popular in applications developed using Ruby, PHP, Perl or Python. Just like Microsoft's SQL-Server is the default database if you are a C# application. This is so because of the community and peer group effect. And also because there are many tools and expertise within the ecosystem if you choose a popular stack. But the most popular language to develop *enterprise* applications is Java. And I personally get more fond of Scala by every passing day. So the hypothesis is, for JVM developers MySQL does not lend well *just* because of the community/peer-group effect. So the choice needs to be based more on technological pro's and con's.

### Quoting from the references

##### There is no reason at all to use MySQL: MariaDB, MySQL founder Michael Widenius

What Oracle is doing wrong (visit the [website](http://www.muktware.com/2013/05/there-is-no-reason-at-all-to-use-MySQL-mariadb-MySQL-founder-michael-widenius/4298) to find the reference for each point)

* New ‘enterprise’ extensions in MySQL are closed source
* The bugs database is not public anymore
* The MySQL public repositories are not anymore actively updated.
* Security problems are not communicated nor addressed quickly (This is making     Linux distributions very annoyed with Oracle)
* Instead of fixing bugs, Oracle is removing features:
* New code in MySQL 5.5 doesn’t have test cases anymore.
* Some of the new code is surprisingly good by Oracle, but unfortunately the quality varies and a notable part needs to be rewritten before we can include it in MariaDB
* And, probably worst of all, it’s impossible for the community to work with the MySQL developers at Oracle.
* Oracle doesn’t accept patches
* There is no public roadmap
* There is no way to discuss with MySQL developers how to implement things or how the current code works