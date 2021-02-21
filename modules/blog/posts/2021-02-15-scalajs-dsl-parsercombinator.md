{{{
    "title": "DSL for UI using Scala Parser Combinators",
    "subheading": "",
    "tags" : [  ],
    "category" : "technology",
    "date" : "15-02-2021",
    "description" : "",
    "toc": true
}}}

Recently I had an opportunity to dwell into an interesting UI problem - with the proliferation of Single Page Application (SPA) as a design pattern for UI development, you sometimes end up with many screens that are not significantly different from each other. In this case the screens were web-forms. Most web-forms have a standard bunch of UI components - textbox's, textarea's, radio-button's, combo-box's et al. Instead of building each of these web-form's as a separate UI project the idea was to roll a DSL with 4 requirements -
1. The impl of this DSL is persisted in a repository/database on the backend to be transported to frontend as-is and get translated in the browser
2. Conditional rendering of components: say user check's a box to find a textbox on the web-form get disabled
3. Fetch data from the backend to populate components: say values in combo-box is dynamic per user
4. I did *not* want to roll my own new parser from grounds-up. Wanted to build upon something that exists in the open-source world to expedite development 

### JavaScript `eval` is Evil
The very first approach I quickly considered and discarded was using JavaScript `eval`. The hypothesis was since there exist JS parsers which are used in IDE's for checking JS correctness (JS lib that parse JS code) - could they even *execute* JS without using the built-in `eval`. Looked at [Acorn](https://github.com/acornjs/acorn), [Esprima](https://esprima.org/) & [Uglify](http://lisperator.net/uglifyjs/) and found they don't provide a 'execute' functionality (and logically so). Also discovered `safe-eval` lib's on NPM are mostly useless and the MDN suggestion of using `Function()` does not work. The basic problem with `eval` itself in JS are plenty including - (a) Prone to injection attacks (b) Debugging is hard (no line number).

### Logical Expressions in JSON
UI developers love JSON. So, why not use JSON itself as the DSL. JSON fit the bill perfectly for requirement #1. Now, could I use the logical expression evaluator lib's in the JSON world for #2? I looked at [JsonLogic](https://jsonlogic.com/) and [Json-Rules-Engine](https://www.npmjs.com/package/json-rules-engine). Tho both these are fine projects, what made me anxious was the verbosity and complexity. The logical expressions were not intuitive and required thorough understanding of the intricacies of these lib's. The language got too verbose too quickly - 2 or 3 levels of nesting was going to make the JSON look monstrous.

### Means to build a DSL in JavaScript
Having built large DSL's in my prior engineering career I wanted to checkout the different parser-combinator and parser-generator frameworks that exist in the world of JavaScript. After all, personally, I was coming back to JavaScript world after about 6 years and the language landscape had changed dramatically for the good. [This blog](https://tomassetti.me/parsing-in-javascript/) by DSL's guru Frederico Tomissetti was very useful in getting started. Based on it and some google search, this is the listing I came up with -

##### Parser Generators: tools that generate parsers
1. [ANTLR](https://www.antlr.org/)
2. [APG](http://www.coasttocoastresearch.com/overview)
3. [Jison](http://zaa.ch/jison/)
4. [Nearley](https://nearley.js.org/)
5. [Canopy](http://canopy.jcoglan.com/)
6. [Ohm](https://github.com/harc/ohm)
7. [PEG](https://pegjs.org/)
8. [Waxeye](https://waxeye.org/)

##### Parser Combinators: libraries to build parsers
1. [Bennu](https://github.com/mattbierner/bennu)
2. [Parsimmon](https://github.com/mattbierner/bennu)
3. [Parjs](https://github.com/GregRos/parjs)
4. [Chevrotain](https://github.com/SAP/chevrotain)

Thinking about **parser-generators**, the following thoughts bothered me -
1. Understanding the flavor of BNF/EBNF supported by these frameworks was a complex, time-taking endeavor. The time and effort it would need would be significantly higher than parser-combinator approach
2. Ad-hoc functions (non-expressions) for fetching data are *not* supported by most parser-generators. Such a capability was needed for requirement #3 - to be able to make both REST and GQL calls from the UI to the backend

### My Approach using ScalaJS & Scala Parser Combinators
Personally, my most favored languages in current times are **Scala** and **Haskell**. My ability to write code is fastest in Scala. And I had heard that [ScalaJS](https://www.scala-js.org/) packs a punch. I also wanted to add the following non-technical requirements -
1. Structurally small 
2. Fluent language: keyword richness, restrictive on structure
3. Grammar interpretation based on proper modeling: Lexer → Parser → AST → Render. Make it easy to extend and validate due to ‘formal’ structure

My interest on this ScalaJS way got a filip when I found that Scala [parser-combinators](https://github.com/scala/scala-parser-combinators#scalajs-and-scala-native), which is a standalone lib, is available and works perfectly well for ScalaJS. So here is what I built on a bright Saturday 3 months ago -

1. Here is what my [grammar of my DSL](https://github.com/bharath12345/ui-parser-combinator/blob/main/src/main/scala/tutorial/webapp/ParserApp.scala#L7) looks like. The ideas behind it are simple -
     * A single grammar file is for an instance of the SPA. And the SPA can have multiple visual 'sections' 
     * Within each section is a bunch of web-form components like TextInput and Select as is obvious
     * DSL supports conditional constructs like IF/THEN/ELSE which can be used to build the dependency among components
2. My [Lexer](https://github.com/bharath12345/ui-parser-combinator/blob/main/src/main/scala/tutorial/webapp/Lexer.scala) uses the powerful `RegexParsers` data type of Scala parser-combinator. The statements in the DSL are tokenized into a bunch of neat instances of `case class/object` - Scala's wonderful support to build ADT helps in giving structure to the token's
3. Next the [Parser](https://github.com/bharath12345/ui-parser-combinator/blob/main/src/main/scala/tutorial/webapp/SchemaParser.scala) strings the tokens to build an AST (abstract syntax tree). We need the AST so that it can be traversed for (a) global validation (b) used for rendering
4. The [Renderer](https://github.com/bharath12345/ui-parser-combinator/blob/main/src/main/scala/tutorial/webapp/Render.scala) is where ScalaJS really shines. It makes it so simple to play with HTML DOM for OO/functional programmers. My renderer traverses the AST and renders the UI with TextBox's/ComboBox's in the UI. I used this simple [HTML page](https://github.com/bharath12345/ui-parser-combinator/blob/main/index-dev.html) (served from any web server of choice, say python or node) to render the UI finally. And it looks like this -
![image](http://bharathwrites.in/assets/images/scalajs.png)

It has some very simple functionality encoded in the DSL. When `textInput3` has value of "kumar" entered, it adds a new TextBox etc

### Final Thoughts
The DSL I built is not complete. I wanted to attempt requirement #3 but could not due to paucity of time. But it should be pretty straightforward to add some new keywords for REST/GQL calls connecting these components, or POSTing their values on form submission. What the project did was open my eyes to the possibilities - both within the Scala ecosystem and how the walls between backend/frontend are being shattered in amazing ways. Finally, it also allowed me to code in Scala after a bit of hiatus :)
 
 

