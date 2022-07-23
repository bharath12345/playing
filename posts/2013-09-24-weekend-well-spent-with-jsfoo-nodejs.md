{{{
    "title": "Weekend well spent with JSFoo and NodeJS",
    "subheading": "",
    "tags" : [ "javascript", "NodeJS", "JSFoo", "blog" ],
    "category" : "technology",
    "date" : "24-09-2013",
    "description" : "",
    "toc": true
}}}

Had been to the wonderful JavaScript conference [JSFoo](https://funnel.hasgeek.com/jsfoo2013/) last week. The tremendous enthusiasm in the web development community for server side JavaScript was all at display. Personally, I have spent a lot of time coding visualizations with JavaScript. However only recently did I write some tidbits of code with NodeJS. And I hadn't spent any time properly studying it. The conference has spurred me to do better. I started reading NodeJS and working on a small project to create my first non-trivial NodeJS application (which I shall share in this blog). But those details are for a little later… let me start with JSFoo…

#### JSFoo
Me being a little bit of computer science fundamentalist have looked upon those doing web development with a little skepticism. Do they really get the data-structures? Complexity of algorithms? Well, those those questions were put to rest by the many speakers at the conference forever. NodeJS might not be in production in a big way as of now. But there is no doubting the quality of people behind it. The design and frameworks are still in the makes… but quality people from the developer community are lapping it up. And the industry is not to be left behind… Of all, Microsoft and Adobe were among those sponsoring the event - Microsoft (with its .Net dream fading slowly) was busy showing off IE10 while Adobe seems to be on its way to burying flash and flex with investments to build open-source JavaScript frameworks… 

For me the conference started on a beautiful note. It had to do with the Mozilla foundation. It was awesome of Mozilla to have brought in such a wonderful contingent for the meet. My first love has always been Firefox. I do all my development on Firefox. Coming to know that Chris Heilmann was among those in the hall made me smile within myself.

Now coming to the few talks that will stay with me…

* [Robert Nyman](http://robertnyman.com/) was among the first speakers. He spoke on the upcoming FirefoxOS for mobile devices. I had my first brush with FirefoxOS at the Wikipedia hackathon and have spent some time with it. Android definitely needs another open-source competitor. With a JavaScript API platform, one hopes, FirefoxOS will catch on with the larger community of web developers. The next step for smartphones is to be able to support 1000s of lightweight apps. I hope that race gets kickstarted with FirefoxOS (there is already a nice '*search app*' facility in FirefoxOS which tells me they have their marker in the right direction!)

* I was really looking forward to listening to [Christian Heilmann](http://christianheilmann.com/). And his choice of topic did not let me down. In his talk he urged the web developers to study HTML5. Developers continue to use shims and jQuery plugin's unnecessarily - the features they look for have made their way into the specs and should be available by default (full screen API as a case for point). Browsers are claiming HTML5 support without fully implementing the specs - and in this situation it becomes the job of the developers to pound on the doors of the browser developers (file bugs) if any part of the spec is unimplemented or glossed over. Personally, let me admit - I have never read a book on HTML5 (for that matter I don't remember if I have ever read any book on HTML at all). If someone had suggested reading a book on HTML5 before this talk I would have responded by saying that I find the W3C resources on the web quite sufficient. But now, after listening to Chris, I know why my thinking is wrong.

* One of the talks that blew my mind was that by [Nilesh Trivedi](http://www.nileshtrivedi.com/) on Interactive Physics Simulations. I would have to watch the video of Nilesh's talk many times over to grasp all that he said. And to build the application that he has without using any pre-built frameworks is absolutely astounding! If you are a C/Java programmer with a liking for theory of computing like me, then, there you have it - there are people like Nilesh in the JavaScript world! 

* The two workshops that I went to were both superbly conducted. [Bharani Muthukumarswamy](http://bharani.herokuapp.com/) introduced me to MeteorJS (and made me promise to myself to try it soon). And in the other workshop [Pankaj Bhageria](https://github.com/panbhag) made me construct the server side of a JavaScript app step-by-step. Both made me code. And I enjoyed it thoroughly.

* Other good talks included the one by Om Shankar on WebRTC, Offline Apps by Manan Bharara, Persona based authentication system (newly being brought by Mozilla) by Francois Marier and the preview of developer tools for the upcoming IE10 by [Rajasekharan Vengalil](http://blogorama.nerdworks.in/).

Before I end my note on JSFoo I must express my *Thanks* to HasGeek. I had been to the Fifth Elephant few months ago and now JSFoo. I must congratulate them for filling what was a definite need among the developer community. Yes we now have www.meetup.com and other hackathons happening ever more regularly. But the Indian software community and developers in particular need more interaction. I have come to learn about so many wonderful small companies and people through these two conferences that I have lost count. *Thank you HasGeek!*

<hr>

#### The making of two blogs…
A decade ago I had a blogspot blog. I used it for a couple of years. Then got tired of it and created a new one on WordPress. But never felt like writing anything of substance on it. With my hacker like attitude (even back then), I always detested the way these blogs looked, the URL itself and many such things. Buying web-server space for hosting a blog felt plain wrong. So a year ago when I came to know of GitHub Pages I decided to try it as soon as I could. GitHub is the ideal platform for engineers to blog. Jekyll is super easy to learn. And for those with version control in their bloodstream and daydreams, Git feels so nice. So I bought my domain name (for less than Rs. 200!) and got started a couple of months ago. And though the blog is not close to what I want it to look like, it still feels so much better than blogspot…

But then that was till last week. One thing that I did not like with GitHub Pages was Ruby. I don't know Ruby. And I have no inclination to learn it. So when I had to understand Gems and Rake it did not feel good. When I got a couple of error emails from GitHub saying that the blog build had failed, it felt worse (though the problems itself were trivial to fix)…

I knew I could use Heroku and host the blog as a NodeJS application while simultaneously putting it in on GitHub. That would give me **server side control**. And a SQL database! And a NoSql database!! So last weekend, while attending the conference, I let the urge to take me over. I started chipping away with my first NodeJS blog app… it is in fairly good shape now… and so… I am happy to present - [http://bharathblogs.in](http://bharathblogs.in)!!

So what sense does it make to have two blogs? None. So what I am going to do? Keep both! Well, the domain name costs nothing. (And I like to build backup plans with my applications!)… The thing is, I have built both and the code is almost identical. So why dismantle either anyway...? 

Now here is a quick primer to how and what of building both these...

<hr>

#### bharathwrites.in
The components -

* GitHub pages for hosting
* Jekyll for static blog generation
* Grunt for JS minify (see the Gruntfile.js for complete list of tasks)
* Twitter Bootstrap, FontAwesome for the blog's look and feel
* Posts use various JavaScript frameworks like Dojo, jQuery, Angular, D3, Stack etc

<hr>

#### bharathblogs.in
The components -

* Heroku for hosting
* GitHub for version control
* NodeJS as the server side platform
* [Poet](http://jsantell.github.io/poet) as the blogging framework
* Grunt for JS minify (see the Gruntfile.js for complete list of tasks)
* Twitter Bootstrap, FontAwesome for the blog's look and feel (etc)
* Posts use various JavaScript frameworks like Dojo, jQuery, Angular, D3, Stack etc

Now I plan to slowly add more functionality to the server-side of my NodeJS blog app. Probably scrape a few RESTful data sources on the web that is of interest to me and hopefully that of my visitors. Start using the Heroku provided MongoDB. And so on… one sad thing is Heroku does not support WebSockets… Else I had couple of interesting ideas for that one. (And one of these days I will probably swap bharathwrites.in to be hosted from Heroku and bharathblogs.in from GitHub pages… want to hack on my NodeJS blog a lot and I like the bharathwrites.in url better) 