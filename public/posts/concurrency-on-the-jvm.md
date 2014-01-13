{{{
    "title": "Concurrency on the JVM",
    "subheading": "",
    "tags" : [ "book", "java" ],
    "category" : "technology",
    "date" : "11-14-2013",
    "description" : "",
    "toc": false
}}}
Over the last few months I amused myself with an interesting pursuit. I spoke to a large number of people on the aspect of concurrency. I spoke to ex-colleagues. I spoke to engineers, architects at hackathons/meetups. And I interviewed a large number of senior engineers for a job at my company. I spoke to them about building a highly-concurrent, high-volume, real-time data-aggregation engine. Gave examples of easy, textbookish projects to drive home the requirements. Like a stock trading platform with 1000s of users, 1000s of stocks and 100s of stock-exchanges. Or a IPL ticket *bidding* site with 1000s of users, many seating categories, many venues etc. And this small article is about my perspectives at the end of it.

<hr>

#### Programming Concurrency... Building Frustrations Globally
The engineering challenge involved in building high volume concurrent applications should not be underestimated. Some *veterans* I spoke to suggested that such problems and multiple solution approaches have existed for ages. I shamelessly, often at the cost of personal repute (of embarrassing someone) counter-questioned to tell me the *various* approaches. Essentially, all, boiled down to just two - 

1. **Handling all data in a single thread due to the fear of complexity**: On hearing this, I would often say do u really intend to exercise just one *core* of your upcoming quad-core, 64-processor server? Hearing this, they would move on to the 2nd option, which is...
2. **Build the beast with thread-pools, locks and synchronisation blocks**: Lets use java.util.concurrent and laugh our way to the ATM, some suggested... and I would say watch out... you could end up in a jail, a mental asylum or a bankruptcy proceeding before reaching that ATM!

While interviewing candidates for the job I have been on the lookout for engineers who have few perspectives *other than* just these two. A small fraction, when pushed, uttered something to the extent of *event-based, SEDA* approaches. But when questioned further, these event based approaches often endup folding in the realm of one of the above two. I find this rather sad. 

Let me clarify, I do **not** think that the above two approaches are fundamentally wrong. But knowing just two is clearly insufficient. 

I also ran into an interesting few who had dabbled with NodeJS and were clearly smitten. Smitten with the action and enthusiasm of engineers in that world rather than with any hard technological breakthroughs. In this article I will not talk about NodeJS. I have briefly written about it earlier on this blog [here](http://bharathwrites.in/posts/the-bleeding-edge-of-an-application/). I dearly hope that those who suggested NodeJS did so out of their own naiveté and my hard nudges... and do not truly believe in the NodeJS performance hyperbole!

<hr>

#### Not Java... But the JVM!
Java engineers need to graduate to becoming JVM engineers. They need to internalise forever the fact that JVM has been a far bigger innovation than Java as a language. And when Java engineers will do that they will realise that the so-called competitors like NodeJS are non-starters. One book that I highly advise to those who wish to make this graduation is the super revealing 'Programming Concurrency on the JVM' by [Dr. Venkat Subramaniam](https://twitter.com/venkat_s). I read this book sometime ago. Back then, I was just beginning to find my reasons to learn Scala/Clojure. Reading it filled me with the energy to know more about the JVM internals and the new world of concurrency programming. 

Nietzche once said "He who has a why to live can bear almost any how". As a programmer, my why has been concurrency, multi-core, big-data and high-performance. And Dr. Venkat gives a few how's!

Broadly, the book is divided into three architectural approaches that one could take to build a concurrent application on the JVM, which are -

1. **java.util.concurrent** with thread-pools, synchronization blocks, locks, fork-join etc 
2. **Software Transactional Memory** - being made popular by Clojure. [This](http://stackoverflow.com/questions/209751/any-real-world-experience-using-software-transactional-memory) StackOverflow thread on real world adoption is instructive. And [this](http://www.cs.rochester.edu/~sandhya/papers/usenix_login_09.pdf) paper gives the reader an excellent understanding from both Hardware and Software Transactional Memory perspectives
3. **Actor Based Concurrency** - being made popular by Scala and Akka. One just needs to visit the Typesafe website to know about the rapid adoption of this model

<hr>

#### Programming Concurrency on the JVM - Dr. Venkat Subramaniam
Dr. Venkat drives home the following points to those who wish to develop concurrent applications -

1. The three options available to the designers
	* Shared mutability... the *pure evil* option
	* Isolated mutability
	* Pure immutability   
<br />
2. Introduction to the world of *persistent* data structures. [Here](http://cstheory.stackexchange.com/questions/1539/whats-new-in-purely-functional-data-structures-since-okasaki) is a mind blowing thread on recent innovations in functional data structures. Many are persistent.

3. A quick intro to the world of modern JDK concurrency mechanisms. It is in this part of the book that I found a certain treatment of the subject of concurrency that I was sorely missing. Applications has multiple 'needs' that drive the concurrency requirement. Broadly these needs can be divided into three parts -
	* High Network I/O Intensity (large network I/O requirements lead to concurrency designs)
	* High Disk I/O Intensity
	* Large compute problems which can be broken down to smaller pieces... divide and conquer... which leads to concurrent designs   
<br />	
4. The numerous code examples in the book showcase two things -
	* Increasing complexity of code in certain approaches
	* The time-to-compute or efficiency differential by comparing the different approaches
<br />
<br />

5. I would love to quote a few sentences from the STM chapter of the book...

	> (1) We’ve been led down the path of the imperative style of programming with mutable state for so long that it’s very hard to see alternatives to synchronization, but there are.
	
	> (2) OOP didn’t quite turn out to be what Alan Kay had in mind when he coined the term. His vision was primarily message passing, and he wanted to get rid of data. Somewhere along the way, OO languages started down the path of data hiding through Abstract Data Types (ADTs), binding data with procedure or combining state and behavior.	
	
	> (3) In the real world, the state does not change; the identity does
	
6. But STM is not a silver bullet to all concurrency applications. The author clearly says - STM is suitable for concurrent reads and infrequent to reasonably frequent write collisions to the same data.

7. Actors are a pure message passing model. Each actor has a built-in message queue. Actor library allows multiple actors to send messages concurrently. The senders are nonblocking by default. Although multiple actors may be active at any time, only one thread is active in an actor at any instance. The main drawback of this model, in my opinion, is that message passing systems with proper interleaving is not an easy art - it requires deep design thinking. 

#### Epilogue
Programming concurrency is hard. Any which way. When confronted with such requirements and problems, the vocabulary used by the engineers and architects to make good design choice and find right hires is extremely critical. I was recently following some discussions on Y! Combinator on the suitability of Scala/Clojure to develop enterprise applications using such new ideas for concurrency and many other things. And I found this comment, though a little harshly worded, as food for thought...

> The only thing enterprise business managers want is a language that can dumb down the art of programming to a level a programmers can be managed like assembly line workers. And that is what Java does exactly, an IDE that can make a novice and expert work at the same levels of productivity, extremely verbose code that gives an illusion of people building something big(even if its down right trivial). And most importantly programming effort can be accounted like a couple of least important replaceable folks down the hierarchy doing some assembling reusable units of material. Change this scenario, a good technology with merit makes programmers very important and makes managers look like desk clerks. Enterprise Managers don't care a least about type systems, lambdas, or traits or whatever. Most managers don't have a remote clue what those things are. Can the technology enable them to manage herds of programmers dumbed down enough to be managed like sheep? That is all they care.