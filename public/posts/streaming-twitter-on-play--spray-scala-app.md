{{{
    "title": "Streaming Twitter On Play + Spray Scala App",
    "subheading": "",
    "tags" : [ "scala", "twitter" ],
    "category" : "technology",
    "date" : "1-29-2014",
    "description" : "",
    "toc": true
}}}

Working on [Scalog](http://bharathwrites.in/posts/scala-projects-in-the-making/#scalog) I decided to write a quick program couple of days ago to see the super trending twitter hashtag - #ArnabVsRahul. I initially tried to follow the hashtag on TweetDeck but found that the arrival rate of new tweets simply did not allow me to read. Wanted a way to read the tweets page-by-page with each page reloading when I refresh. So wrote a program to do so - A twitter stream listener! And yesterday pushed the code to GitHub and this is a quick post on it. The code itself can be found [here](https://github.com/bharath12345/playing).

Am building Scalog using [Play2](http://www.playframework.com/)! framework in Scala. The blog hosted on Heroku can be accessed by this link - [http://bharathplays.herokuapp.com/](http://bharathplays.herokuapp.com/). The blog itself is just a Scala replica of my Jekyll and NodeJS blogs. Nothing special in the blog part.

To listen to the tweets I use [Spray's](http://spray.io/) HTTP actor listeners. The Spray HTTP client connects to Twitter's stream service URL and waits for the chunked responses on a persistent connection. Every new tweet arrives as a chunk. I simply push the tweets to a ByteArrayStream and read it later in Play's [streaming](http://www.playframework.com/documentation/2.2.x/ScalaStream) to send it to a requesting browser.

My twitter streamer can be accessed by using this link stub - http://bharathblogs.herokuapp.com/twitter/go/ followed by the term to search. For example to read tweets with ArnabVsRahul one will have to use the URL - [http://bharathplays.herokuapp.com/twitter/go/ArnabVsRahul](http://bharathplays.herokuapp.com/twitter/go/ArnabVsRahul)

The URL needs to refreshed once for the streaming to actually begin. The first access returns no data just as a check. And not necessarily all search strings will produce results - so its better to search for permanently high trending strings like "india" in case you see no data.

[This blog](http://www.cakesolutions.net/teamblogs/2013/12/08/streaming-twitter-api-in-akka-and-spray/) came in handy while trying to understand how to stream twitter data using Spray's HTTP client's capabilities.



