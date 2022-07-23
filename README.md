My Play & React based blog 

## Useful Heroku Commands
```
% heroku stack:set cedar-14 -a bharathplays
% git push heroku master
% heroku config:set SBT_CLEAN=true
% heroku config:unset SBT_OPTS
% heroku open
% heroku logs --tail
% heroku ps
% heroku ps:scale web=1
```

## Main part
 - `FrontendController` is rendering the React part's index.html
 - Play framework part of the application is the backend, runs on port 9000; it responds via JSON responses
 - React framework part of the application is the frontend, runs on port 3000; it queries the backend via `localhost` fetch queries (in Client.js)
 - 

## Api Responses

### http://localhost:9000/api/homepage
```json
{
  "homepage": [
    [
      "2021-02-15-scalajs-dsl-parsercombinator",
      {
        "date": "15-02-2021",
        "content": "<p>Recently I had an opportunity to dwell into an interesting UI problem - with the proliferation of Single Page Application (SPA) as a design pattern for UI development, you sometimes end up with many screens that are not significantly different from each other. In this case the screens were web-forms. Most web-forms have a standard bunch of UI components - textbox's, textarea's, radio-button's, combo-box's et al. Instead of building each of these web-form's as a separate UI project the idea was to roll a DSL with 4 requirements -</p>",
        "title": "DSL for UI using Scala Parser Combinators",
        "path": "post/2021-02-15-scalajs-dsl-parsercombinator"
      }
    ],
    [
      "2014-10-17-functional-conference-random-notes",
      {
        "date": "17-10-2014",
        "content": "<p>The first '<a href=\"http://functionalconf.com/\">Functional Conference</a>' happened in Bangalore between Oct 9-11. I had been keenly looking forward to it. This is a quick post on the sessions I attended and the conference itself. As the lineup of speakers and topics shaped up in the buildup to the conference on their website, it heightened my expectations. As a younger engineer I have gone through the cycle of expecting too much from conferences and thus not being able to learn sufficiently from that which was on offer. Time has had a mellowing effect... I find it much better to keep an open mind and try to absorb all that is on offer. And then, a little later, retain only that which is useful/pertinent. With that mindset and approach I found 'Functional Conference' a very fulfilling technology experience - plenty of technical richness to absorb and sufficient ideas to retain for long.</p>",
        "title": "Functional Conference - Random Notes",
        "path": "post/2014-10-17-functional-conference-random-notes"
      }
    ]
  ]
}
```

### http://localhost:9000/api/blog/2021-02-15-scalajs-dsl-parsercombinator
```json
{
  "blog": {
    "title": "DSL for UI using Scala Parser Combinators",
    "content": "<p>Recently I had an opportunity to dwell into an interesting UI problem - with the proliferation of Single Page Application (SPA) as a design pattern for UI development, you sometimes end up with many screens that are not significantly different from each other. In this case the screens were web-forms. Most web-forms have a standard bunch of UI components - textbox's, textarea's, radio-button's, combo-box's et al. Instead of building each of these web-form's as a separate UI project the idea was to roll a DSL with 4 requirements -</p>\n<ol>\n  <li>The impl of this DSL is persisted in a repository/database on the backend to be transported to frontend as-is and get translated in the browser</li>\n  <li>Conditional rendering of components: say user check's a box to find a textbox on the web-form get disabled</li>\n  <li>Fetch data from the backend to populate components: say values in combo-box is dynamic per user</li>\n  <li>I did <em>not</em> want to roll my own new parser from grounds-up. Wanted to build upon something that exists in the open-source world to expedite development</li>\n</ol><h3>JavaScript <code>eval</code> is Evil</h3><p>The very first approach I quickly considered and discarded was using JavaScript <code>eval</code>. The hypothesis was since there exist JS parsers which are used in IDE's for checking JS correctness (JS lib that parse JS code) - could they even <em>execute</em> JS without using the built-in <code>eval</code>. Looked at <a href=\"https://github.com/acornjs/acorn\">Acorn</a>, <a href=\"https://esprima.org/\">Esprima</a> &amp; <a href=\"http://lisperator.net/uglifyjs/\">Uglify</a> and found they don't provide a 'execute' functionality (and logically so). Also discovered <code>safe-eval</code> lib's on NPM are mostly useless and the MDN suggestion of using <code>Function()</code> does not work. The basic problem with <code>eval</code> itself in JS are plenty including - (a) Prone to injection attacks (b) Debugging is hard (no line number).</p><h3>Logical Expressions in JSON</h3><p>UI developers love JSON. So, why not use JSON itself as the DSL. JSON fit the bill perfectly for requirement #1. Now, could I use the logical expression evaluator lib's in the JSON world for #2? I looked at <a href=\"https://jsonlogic.com/\">JsonLogic</a> and <a href=\"https://www.npmjs.com/package/json-rules-engine\">Json-Rules-Engine</a>. Tho both these are fine projects, what made me anxious was the verbosity and complexity. The logical expressions were not intuitive and required thorough understanding of the intricacies of these lib's. The language got too verbose too quickly - 2 or 3 levels of nesting was going to make the JSON look monstrous.</p><h3>Means to build a DSL in JavaScript</h3><p>Having built large DSL's in my prior engineering career I wanted to checkout the different parser-combinator and parser-generator frameworks that exist in the world of JavaScript. After all, personally, I was coming back to JavaScript world after about 6 years and the language landscape had changed dramatically for the good. <a href=\"https://tomassetti.me/parsing-in-javascript/\">This blog</a> by DSL's guru Frederico Tomissetti was very useful in getting started. Based on it and some google search, this is the listing I came up with -</p><h5>Parser Generators: tools that generate parsers</h5>\n<ol>\n  <li><a href=\"https://www.antlr.org/\">ANTLR</a></li>\n  <li><a href=\"http://www.coasttocoastresearch.com/overview\">APG</a></li>\n  <li><a href=\"http://zaa.ch/jison/\">Jison</a></li>\n  <li><a href=\"https://nearley.js.org/\">Nearley</a></li>\n  <li><a href=\"http://canopy.jcoglan.com/\">Canopy</a></li>\n  <li><a href=\"https://github.com/harc/ohm\">Ohm</a></li>\n  <li><a href=\"https://pegjs.org/\">PEG</a></li>\n  <li><a href=\"https://waxeye.org/\">Waxeye</a></li>\n</ol><h5>Parser Combinators: libraries to build parsers</h5>\n<ol>\n  <li><a href=\"https://github.com/mattbierner/bennu\">Bennu</a></li>\n  <li><a href=\"https://github.com/mattbierner/bennu\">Parsimmon</a></li>\n  <li><a href=\"https://github.com/GregRos/parjs\">Parjs</a></li>\n  <li><a href=\"https://github.com/SAP/chevrotain\">Chevrotain</a></li>\n</ol><p>Thinking about <strong>parser-generators</strong>, the following thoughts bothered me -</p>\n<ol>\n  <li>Understanding the flavor of BNF/EBNF supported by these frameworks was a complex, time-taking endeavor. The time and effort it would need would be significantly higher than parser-combinator approach</li>\n  <li>Ad-hoc functions (non-expressions) for fetching data are <em>not</em> supported by most parser-generators. Such a capability was needed for requirement #3 - to be able to make both REST and GQL calls from the UI to the backend</li>\n</ol><h3>My Approach using ScalaJS &amp; Scala Parser Combinators</h3><p>Personally, my most favored languages in current times are <strong>Scala</strong> and <strong>Haskell</strong>. My ability to write code is fastest in Scala. And I had heard that <a href=\"https://www.scala-js.org/\">ScalaJS</a> packs a punch. I also wanted to add the following non-technical requirements -</p>\n<ol>\n  <li>Structurally small</li>\n  <li>Fluent language: keyword richness, restrictive on structure</li>\n  <li>Grammar interpretation based on proper modeling: Lexer → Parser → AST → Render. Make it easy to extend and validate due to ‘formal’ structure</li>\n</ol><p>My interest on this ScalaJS way got a filip when I found that Scala <a href=\"https://github.com/scala/scala-parser-combinators#scalajs-and-scala-native\">parser-combinators</a>, which is a standalone lib, is available and works perfectly well for ScalaJS. So here is what I built on a bright Saturday 3 months ago -</p>\n<ol>\n  <li>Here is what my <a href=\"https://github.com/bharath12345/ui-parser-combinator/blob/main/src/main/scala/tutorial/webapp/ParserApp.scala#L7\">grammar of my DSL</a> looks like. The ideas behind it are simple -\n  <ul>\n    <li>A single grammar file is for an instance of the SPA. And the SPA can have multiple visual 'sections'</li>\n    <li>Within each section is a bunch of web-form components like TextInput and Select as is obvious</li>\n    <li>DSL supports conditional constructs like IF/THEN/ELSE which can be used to build the dependency among components</li>\n  </ul></li>\n  <li>My <a href=\"https://github.com/bharath12345/ui-parser-combinator/blob/main/src/main/scala/tutorial/webapp/Lexer.scala\">Lexer</a> uses the powerful <code>RegexParsers</code> data type of Scala parser-combinator. The statements in the DSL are tokenized into a bunch of neat instances of <code>case class/object</code> - Scala's wonderful support to build ADT helps in giving structure to the token's</li>\n  <li>Next the <a href=\"https://github.com/bharath12345/ui-parser-combinator/blob/main/src/main/scala/tutorial/webapp/SchemaParser.scala\">Parser</a> strings the tokens to build an AST (abstract syntax tree). We need the AST so that it can be traversed for (a) global validation (b) used for rendering</li>\n  <li>The <a href=\"https://github.com/bharath12345/ui-parser-combinator/blob/main/src/main/scala/tutorial/webapp/Render.scala\">Renderer</a> is where ScalaJS really shines. It makes it so simple to play with HTML DOM for OO/functional programmers. My renderer traverses the AST and renders the UI with TextBox's/ComboBox's in the UI. I used this simple <a href=\"https://github.com/bharath12345/ui-parser-combinator/blob/main/index-dev.html\">HTML page</a> (served from any web server of choice, say python or node) to render the UI finally. And it looks like this - <img src=\"http://bharathwrites.in/assets/images/scalajs.png\" alt=\"image\"\"/></li>\n</ol><p>It has some very simple functionality encoded in the DSL. When <code>textInput3</code> has value of \"kumar\" entered, it adds a new TextBox etc</p><h3>Final Thoughts</h3><p>The DSL I built is not complete. I wanted to attempt requirement #3 but could not due to paucity of time. But it should be pretty straightforward to add some new keywords for REST/GQL calls connecting these components, or POSTing their values on form submission. What the project did was open my eyes to the possibilities - both within the Scala ecosystem and how the walls between backend/frontend are being shattered in amazing ways. Finally, it also allowed me to code in Scala after a bit of hiatus :)</p>",
    "date": "15-02-2021",
    "subheading": null,
    "toc": true
  }
}
```

### http://localhost:9000/api/page/1
```json
{
  "page": [
    [
      "2014-03-13-play2-on-jboss-wildfly",
      {
        "date": "13-03-2014",
        "content": "<p><a href=\"http://en.wikipedia.org/wiki/Java_Platform,_Enterprise_Edition\">JavaEE</a> (v5 and v6) has a commanding presence in both marketshare and (developer) mindshare in the enterprise software world. The specifications are well thought-out, battle-tested and highly relied upon. I started using JavaEE (v5) way back in 2007 with JBoss 4.x. The latest release, <a href=\"http://www.oracle.com/technetwork/java/javaee/tech/index.html\">JavaEE-7</a>, which was released close to a year ago brings with itself a lot of worthy changes to the specs and impl. To bring myself up to speed on it I went through few books and attended a conference (JUDCon, Bangalore). But I have also been coding and acquainting myself with Typesafe's Scala <em><a href=\"https://typesafe.com/platform\">reactive</a></em> stack. These two stacks are bound to compete with each more and more in the coming days. However I feel, they can be used in applications in complementary ways when carefully designed. The competition and challenge to JavaEE-7 stems from two tough requirements -</p>",
        "title": "Play2 Application on Wildfly - Why and How",
        "path": "post/2014-03-13-play2-on-jboss-wildfly"
      }
    ],
    [
      "2014-02-27-my-scala-application---twitter-volume-grapher-for-indian-election-personalities",
      {
        "date": "27-02-2014",
        "content": "<p>The Indian general elections are around the corner. For software engineers, this time around, there is data to play with and try to predict the outcome. Among all, the data from the social media giants - Twitter and Facebook - is easily accessible for analysis. Though social media may not be the right barometer to judge voter sentiments in a country as big and diverse as India, it is nonetheless a very tempting datasource for anyone curious. So couple of days ago I decided to do a small project - to simply <em>chart</em> the volume of tweets with strings like <em>\"modi\", \"rahul\", \"kejri\" and \"india\"</em> in it. I thought just a graph of volumes by itself will be interesting to see. So here I present the v1.0 of my <strong>Indian-general-elections-social-media-tracker!</strong></p>",
        "title": "My Scala Application - Real-time Twitter Volume Grapher For Indian Elections 2014",
        "path": "post/2014-02-27-my-scala-application---twitter-volume-grapher-for-indian-election-personalities"
      }
    ]
  ]
}
```

### http://localhost:9000/api/tags
```json
{
  "blog": [
    "laws",
    "visualization",
    "scala",
    "twitter",
    "jsplumb",
    "graphs",
    "javascript",
    "jvm",
    "database",
    "algorithms",
    "apm",
    "jsfoo",
    "play",
    "structures",
    "aphorisms",
    "d3",
    "book",
    "data",
    "dojo",
    "wildfly",
    "data-center",
    "websocket",
    "coursera",
    "cassandra",
    "framework",
    "nodejs",
    "java",
    "jboss",
    "blog"
  ]
}
```

### http://localhost:9000/api/tag/javascript
```json
{
  "blog": [
    [
      "2013-09-24-weekend-well-spent-with-jsfoo-nodejs",
      {
        "date": "24-09-2013",
        "content": "<p>Had been to the wonderful JavaScript conference <a href=\"https://funnel.hasgeek.com/jsfoo2013/\">JSFoo</a> last week. The tremendous enthusiasm in the web development community for server side JavaScript was all at display. Personally, I have spent a lot of time coding visualizations with JavaScript. However only recently did I write some tidbits of code with NodeJS. And I hadn't spent any time properly studying it. The conference has spurred me to do better. I started reading NodeJS and working on a small project to create my first non-trivial NodeJS application (which I shall share in this blog). But those details are for a little later… let me start with JSFoo…</p>",
        "title": "Weekend well spent with JSFoo and NodeJS",
        "path": "post/2013-09-24-weekend-well-spent-with-jsfoo-nodejs"
      }
    ],
    [
      "2013-09-11-the-bleeding-edge-of-an-application",
      {
        "date": "11-09-2013",
        "content": "<p>Most web applications have the well-known 3-tiered structure - WebTier &gt; ApplicationTier &gt; DataTier. Both WebTier and ApplicationTier have the web-layer to parse the incoming HTTP requests. Its in the WebTier that one deploy's load-balancing L4-routers like Apache/Nginx or Netscaler like appliances. HTTP requests are forwarded by the WebTier to the ApplicationTier which is generally served by a much bigger farm of servers. Web-layer in the ApplicationTier is the focus of this blog. Its a challenging area of software development for the following reasons and more - </p>",
        "title": "The Bleeding Edge Of A Web Application...",
        "path": "post/2013-09-11-the-bleeding-edge-of-an-application"
      }
    ]
  ]
}
```