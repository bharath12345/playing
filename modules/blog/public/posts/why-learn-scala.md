{{{
    "title": "Why Learn Scala?",
    "subheading": "",
    "tags" : [ "" ],
    "category" : "technology",
    "date" : "12-15-2013",
    "description" : "",
    "toc": true
}}}

It was a long time ago that I read this masterpiece by the software engineering guru, Peter Norvig - [Teach Yourself Programming In Ten Years](http://norvig.com/21-days.html#answers). Peter advises wannabe programmers to learn at least half a dozen programming languages. Taking stock of myself earlier this year I realised having terribly missed out. In my decade long career, I have worked deeply in only 4 languages - C++, Java, JavaScript and Perl. And *none* of them strongly functional per Peter's advise (functional JavaScript hasn't come to me yet). This led me to pose two questions to myself -

* Why do I need another language? 
* If I have to pick one, then, which one?

The answer to the first question came to me rather quickly. At that time I was exploring what was new with JVM-7. And what was expected of Java and JVM-8. JVM-7 with its *invoke dynamic* and Java-8 with *lambdas* were clearly pointing the finger in a certain direction. I realised the designers of the JVM had started to embrace polyglot and functional programming. Digging deeper, the reasons for this move were easy to realise. Java's issues with type-safety, lack of immutable collections (in the JDK), rampant usage of shared mutability etc., were beginning to weigh heavy. The distributed, multicore, big-data computing, realtime world were making Java a little too verbose, justifying the need to look for alternatives.

Surprisingly, the second question turned out to be the tougher of the two. The choice essentially was between Groovy, Scala and Clojure. I chose Scala. My pre-learning decision has got richly rewarded by what I have learnt after taking the plunge to Scala. Even as I continue to make the (sometimes) steep climb, this is a small, humble attempt to articulate the amazing things I have learnt. This write-up is a little too theoretical. For *show-me-the-code* types, I will soon write about a *not-so-small* 3-tier (DB <=> Biz <=> UI) application I have built entirely with Scala. 

In this post I allude to three broad reasons to *Why Learn Scala* -

1. Scala is a great mix. It imbibes some of the best features of other popular, successful languages
2. Scala ecosystem of frameworks/libraries is big, mature, created by some great people from academia/industry and very well documented and supported
3. Some features of Scala that have made me a more thoughtful, better programmer

<hr>

### Scala Is A Great Mix 

Scala brings many new original ideas with it for a Java programmer. New ideas like the implementation of persistent data-structures on the JVM, mixing object-oriented with functional, deconstructing objects with pattern matching and many refreshing ideas to lessen code verbosity. But given its academic roots, these were expected. Whats nice is that Scala also brings with itself some of the best features from at least 4 other popular, well designed programming languages -

1. Haskell
2. Erlang
3. C#
4. Java

Now to quickly dig into what it brings from each of these.

#### Haskell and Scala
These are two interesting Hammer Principle surveys -

* [Learning This Language Improved My Ability As A Programmer](http://hammerprinciple.com/therighttool/statements/learning-this-language-improved-my-ability-as-a-pr)
* [Learning This Language Significantly Changed How I Use Other Languages](http://hammerprinciple.com/therighttool/statements/learning-this-language-significantly-changed-how-i)

Haskell tops both these lists. Scala, at its very core, incorporates a lot of Haskell's good features into itself. Here is a short quick list -

1. Type Inference
2. First Class Functions
3. Currying
4. Lazy Evaluation
5. List Comprehensions
6. Immutability
7. Algebraic Data Types
8. Higher Order Types
9. Monads

One question that begs an answer - If Haskell is so good, then why not use Haskell itself? Why go for Scala? If thats an option then I would definitely encourage the reader to go ahead. But to those like me who love and trust the JVM, want interoperability with Java for its ecosystem of libraries and have a overarching/indefinite need for platform independency, Scala is a welcome choice.

#### Erlang and Scala
[WhatsApp](http://highscalability.com/blog/2013/11/8/stuff-the-internet-says-on-scalability-for-november-8th-2013.html?SSLoginOk=true) gets more messages than Twitter. WhatsApp is built on Erlang. And thats for a reason. To handle as many messages as WhatsApp does, you need a massively concurrent application. To run a massively concurrent application, you need a lot of parallel execution. And Actor based method for concurrency brought by Erlang is built for such a usecase. It is backed by solid theory and research. However Erlang takes a thread backed method for its Actor model's concurrency. And the Erlang process is very lightweight. Erlang applications commonly have tens-of thousands of threads or more. Now thread's are a scarce resource on commodity hardware (Erlang does not always run on commodity hardware). And in a distributed, horizontally scaling setup the constraint on number of threads can be quite strict. The developers of Scala have thus provided two types of Actors: thread-based and event based. Thread based actors execute in heavyweight OS threads. They never block each other, but they don’t scale to more than a few thousand actors per VM. Event-based actors are simple objects. They are very lightweight, and, like Erlang processes, you can spawn millions of them on a modern commodity machine. The difference with Erlang processes is that within each OS thread, event based actors execute sequentially without preemptive scheduling. This makes it possible for an event-based actor to block its OS thread for a long period of time (perhaps indefinitely).

If one is looking to engineer a highly concurrent application on the JVM, then Scala's Actor model provides a compelling option for designing such a system. I encourage the readers to listen to the many videos/talks by the architects of Scala Actor model (like Jonas Boner and Roland Kuhn) to get a more thorough understanding of the Actor model. Scala's Akka library with its Actor model is a great effort to bring the best of Erlang's proven concurrency model to the JVM engineers.

#### C#, Java and Scala
Scala has taken a lot of good things from C# and Java, especially in the syntax area. The syntax seems to have been designed especially keeping the Java programmers in mind, all the while trying to reduce the verbosity. One of the very interesting features that seems to have been inspired by C# is [Implicits](http://www.artima.com/pins1ed/implicit-conversions-and-parameters.html). They provide a means to extend libraries, help in type conversion etc. 

<hr>

### Scala Ecosystem
Scala has had its set of woes in this area. There has been quite some furore over backward compatibility of Scala's native libraries and other frameworks over the last few releases 2.7 > 2.8 > 2.9 > 2.10 (present). The Actors model has been written multiple times over - once as native scala.actors, once as part of the Lift library, and finally as part of Akka. However, having started coding with Scala many months ago and having worked on the latest releases of many of these libraries I have felt them being no different than those that exist in the world of Java in documentation, community backup etc. One great joy is actually the existence of many options in every area of the language, which I illustrate in the list below. 

1. **Concurrency, Event Management, ESB**
    a. [Akka](https://github.com/akka/akka) - actor based concurrency model
    b. [Eventsourced](https://github.com/eligosource/eventsourced) - persistence, recovery, redelivery of messages
2. **Data structures**
    a. [Scalaz](https://github.com/scalaz/scalaz) -  data Structures for functional programming
    b. [RxJava](https://github.com/Netflix/RxJava) - composing asynchronous and event-based programs using observable sequences (not written in Scala but, probably, better used with Scala than Java from code hygiene POV!)
3. **Build and Testing**
    a. [ScalaCheck](https://github.com/rickynils/scalacheck) - testing framework with probably no Java equivalent (at least that I know of)
    b. [SBT](https://github.com/sbt/sbt) - more concise than Maven. No XML crap - build instructions as Scala DSL
4. **Object Relational Mapping** -
    a. [Slick](https://github.com/slick/slick) - Database access
5. **Distributed Big Data Tasks**
    a. [Finagle](https://github.com/twitter/finagle) - Fault tolerant, protocol agnostic RPC system
    b. [Scalding](https://github.com/twitter/scalding) - MapReduce for Scala
    c. [SummingBird](https://github.com/twitter/summingbird) - Streaming, continuous, real-time MapReduce on top of Scalding or Storm
6. **Web Development**
    a. [Lift](http://liftweb.net/)
    b. [Play!](http://www.playframework.com/) 
    c. [Spray.IO](http://spray.io/)

These are but just a few of the popular libraries in some of the more frequently programmed areas. There are many more options for a interested programmer in each area. For example, the number of web development frameworks in native Scala number more than 10. And then there are libraries in other areas like machine-learning etc.

<hr>

### Unlearning and Relearning Programming
For those coming from Java, with no functional programming background, Scala can be a steep learning curve. But it is well worth the effort. To me, apart from exposure to many concepts totally new, Scala has helped in getting more firmly grounded in the fundamentals of structure and interpretation of computer programs. It has helped me realise the many things I need to *unlearn* to become a better programmer! If a passing reader finds this claim interesting, here is a quick list of things I feel better programming at now...

1. Immutability: tradeoffs in using the C/Java style innocuous looking *for* loop; Utility (and at times necessity) of immutable collections (which do not exist in Oracle's Java JDK)
2. Type safety: strengths of Java/JVM style strict typing; [Problems](http://code.stephenmorley.org/articles/java-generics-type-erasure/) in Java's type safety offering
3. Inheritance: a better understanding of covariance and contra-variance 
4. Rethinking code verbosity by composing higher order functions, partial functions etc (lesser code often translates to fewer bugs)
5. A better way to alleviate null-checks using Options
6. Dependency Injection without annotations or XMLs
7. Things can be better than using *static* classes, methods, variables
8. Closures and Mixin's possible on JVM too (until now, I had thought of these only from the JavaScript perspective)
9. Using *Map* when I needed *Tuple* was not exactly a bright idea
10. I can do so much more when I can write code that my build system understands... looking for Maven plugins need not be a way of life...

... and I can go on and on!! 
