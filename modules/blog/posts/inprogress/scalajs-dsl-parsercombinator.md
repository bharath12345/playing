{{{
    "title": "DSL for UI using Scala Parser Combinators",
    "subheading": "",
    "tags" : [  ],
    "category" : "technology",
    "date" : "15-02-2021",
    "description" : "",
    "toc": true
}}}

Recently I had an opportunity to brainstorm an interesting UI problem with my team - with the proliferation of Single Page Application as a design pattern for UI development, you sometimes end up with a large number of screens that are not significantly different from each other. In my case the screens were web-forms. Most web-forms have a standard bunch of UI components - textbox's, textarea's, radio-button's, combo-box's et al. Instead of building each of these web-form's as a separate UI project we considered rolling a DSL. We narrowed down 5 requirements for this DSL project -
1. Each UI instance per this DSL is persisted in a repository or database in the backend. It is transported to frontend as-is and gets translated in the browser
2. It should be able to render UI components from Adobe's fantasic '[React Spectrum library](https://react-spectrum.adobe.com/react-spectrum/)'
3. Conditional rendering of components: say user check's a box and a textbox gets disabled
4. Fetch data from backend to populate components: say values in combo-box is dynamic per user
5. We did not want to roll our own new parser from grounds-up. We wanted to build upon something that exists in the JS open-source world to expedite development 

# JavaScript `eval` is Evil
The very first approach we quickly considered and discarded was using JavaScript `eval`. The hypothesis was since there exist JS parsers which are used in IDE for checking JS correctness (JS lib that parses JS code) - could they even execute JS without using the built-in `eval`. We looked at Acorn, Esprima & Uglify and found they dont provide a 'execute' functionality (and logically so). We also discovered `safe-eval` lib's on NPM are mostly useless and the MDN suggestion of using `Function()` does not work. The basic problem with `eval` itself in JS are plenty including - (a) Prone to injection attacks (b) Debugging is hard (no line number).


