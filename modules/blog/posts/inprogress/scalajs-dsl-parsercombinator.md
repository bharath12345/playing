{{{
    "title": "DSL for UI using Scala Parser Combinators",
    "subheading": "",
    "tags" : [  ],
    "category" : "technology",
    "date" : "15-02-2021",
    "description" : "",
    "toc": true
}}}

Recently I had an opportunity to brainstorm an interesting UI problem with my team - with the proliferation of Single Page Application (SPA) as a design pattern for UI development, you sometimes end up with many screens that are not significantly different from each other. In my case the screens were web-forms. Most web-forms have a standard bunch of UI components - textbox's, textarea's, radio-button's, combo-box's et al. Instead of building each of these web-form's as a separate UI project we considered rolling a DSL. We narrowed down 5 requirements for this DSL project -
1. Each UI instance per this DSL is persisted in a repository or database in the backend. It is transported to frontend as-is and gets translated in the browser
2. It should be able to render UI components from Adobe's fantastic [React Spectrum library](https://react-spectrum.adobe.com/react-spectrum/)
3. Conditional rendering of components: say user check's a box and a textbox gets disabled
4. Fetch data from the backend to populate components: say values in combo-box is dynamic per user
5. We did *not* want to roll our own new parser from grounds-up. We wanted to build upon something that exists in the JS open-source world to expedite development 

# JavaScript `eval` is Evil
The very first approach we quickly considered and discarded was using JavaScript `eval`. The hypothesis was since there exist JS parsers which are used in IDE's for checking JS correctness (JS lib that parse JS code) - could they even *execute* JS without using the built-in `eval`. We looked at [Acorn](https://github.com/acornjs/acorn), [Esprima](https://esprima.org/) & [Uglify](http://lisperator.net/uglifyjs/) and found they don't provide a 'execute' functionality (and logically so). We also discovered `safe-eval` lib's on NPM are mostly useless and the MDN suggestion of using `Function()` does not work. The basic problem with `eval` itself in JS are plenty including - (a) Prone to injection attacks (b) Debugging is hard (no line number).

# Logical Expressions in JSON
The bunch of developers that I was discussing this problem were all UI developers - JavaScript/TypeScript ninja's. And they loved JSON. So another thought that came up was to use JSON itself as the DSL. JSON fit the bill perfectly for requirements #1 and #2. And we could use the logical expression evaluator lib's in the JSON world for #3, was the thought. I looked at [JsonLogic](https://jsonlogic.com/) and [Json-Rules-Engine](https://www.npmjs.com/package/json-rules-engine). Tho both these are fine projects, what made me anxious was the verbosity and complexity. The logical expressions were not intuitive and required thorough understanding of the intricacies of these lib's. The language got too verbose too quickly - 2 or 3 levels of nesting was going to make the JSON look monstrous.

# Means to build a DSL in JavaScript
Having built large DSL's in my prior engineering career I wanted to checkout the different parser-combinator and parser-generator frameworks that exist in the world of JavaScript. After all, personall, I was coming back to JavaScript world after about 6 years and the language landscape had changed dramatically for the good. [This blog](https://tomassetti.me/parsing-in-javascript/) by DSL's guru Frederico Tomissetti was very useful in getting started. Based on it and some more google search, this is the listing I came up with -

##### Parser Generators: tools that generate parsers
1. ANTLR: https://www.antlr.org/
2. APG: http://www.coasttocoastresearch.com/overview
3. Jison: http://zaa.ch/jison/
4. Nearley: https://nearley.js.org/
5. Canopy: http://canopy.jcoglan.com/
6. Ohm: https://github.com/harc/ohm
7. PEG: https://pegjs.org/
8. Waxeye: https://waxeye.org/

##### Parser Combinators: libraries to build parsers
1. Bennu: https://github.com/mattbierner/bennu
2. Parsimmon: https://github.com/mattbierner/bennu
3. Parjs: https://github.com/GregRos/parjs
4. Chevrotain: https://github.com/SAP/chevrotain

Thinking about parser-generators, the following thoughts bothered me -
1. Understanding the flavor of BNF/EBNF supported by any of these frameworks is a complex endeavor. With so many options, to R&D required to know what fits best (get to a thorough understanding of the trade-off's) was no mean ask too. Tho very satisfying as an engineer, the time and effort it would need would be significantly higher than parser-combinator approach
2. Ad-hoc functions (non-expressions) for fetching data are *not* supported by most of the parser-generators. We thought we would need such a capability for requirement #4 - to be able to make both REST and GQL calls from the UI to the backend
3. Questions like will the underlying library continue to get maintained. The effort it takes for newbie's to understand BNF/EBNF

# My Approach using ScalaJS & Scala Parser Combinators
Personally, my most favored languages in current times are Scala and Haskell. My ability to write code is fastest in Scala. And ScalaJS packs a punch. I also wanted to add the following non-technical requirements -

1. Structurally small 
2. Fluent language: Keyword richness, Restrictive on structure
3. Grammar interpretation based on proper modeling: Lexer → Parser → AST → Render. Make it easy to extend and validate due to ‘formal’ structure

