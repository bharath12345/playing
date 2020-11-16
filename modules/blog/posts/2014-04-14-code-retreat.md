{{{
    "title": "Code Retreat",
    "subheading": "",
    "tags" : [  ],
    "category" : "technology",
    "date" : "04-14-2014",
    "description" : "",
    "toc": true
}}}

Last Saturday I happened to go to my first code retreat. Hosted by [Multunus](http://www.multunus.com/) and conducted by the master programmer [Venkat Subramaniam](https://twitter.com/venkat_s), it was a very learnful experience. Here's a sneak-peek of the event from my eyes. 

### The Format
Code retreats are day long coding fests. Programmers take a crack at solving [Conway's Game of Life](http://en.wikipedia.org/wiki/Conway's_Game_of_Life) in multiple short sessions. Each session is for 45 minutes. Developers work in pairs of two (miniature [extreme programming](http://en.wikipedia.org/wiki/Extreme_programming) sessions). Every session starts with a clean slate, that is NO previous code at all. [TDD](http://en.wikipedia.org/wiki/Test-driven_development) is encouraged. After every 45 minute session is a 15 minute standup where everyone get to share the experience and things learnt. And at the end of the standup the group may decide to impose some constraints for the next session... like not-using-classes, no-global-state, no-returns-from-any-methods etc etc. The choice of programming language, frameworks used and design is all left to the pairing team. This meetup had close to 25 programmers who kept up the hacking whole day.

Generally the 45 minutes time slot is insufficient to solve the puzzle along with writing tests. Especially if one is a newbie to the puzzle or language chosen or design idea. However, the the learning is not in solving the puzzle itself... but it lies in the experience of trying to do so within the setting. 

One can read more about the format and structure of Code Retreat at the community website - [http://coderetreat.org](http://coderetreat.org/)

### My Experience
In 5 sessions across the day, I, along with my partner, programmed in 4 languages - Scala, JavaScript, Python and Ruby! Here is a brief on each of my sessions...

My first session was in Scala. My partner and I both had prior experience with the language. And thankfully my partner had some perspective and prior experience with the problem too. In a quick discussion we zeroed in on a subset of the problem we wanted to tackle (neighbour-finding in Game of Life) and agreed on a simple design. We leapt into coding starting with writing the tests. The test part straightaway exposed my poor knowledge of ScalaTest. Late while writing the code we got stuck in trying to write object equality in Scala - something I could do with my eyes closed in Java. That was the second exposé. We programmed on my partner's system and he used gradle - which was a first for me.

JavaScript was common lingo with my next partner in the second session. Neither of us had used any testing frameworks in JS ever. That straightaway was an exposé! Since we did not have time to learn a new framework we decided to hand-code the tests. I don't remember the code now but we wrote some code that seemed to solve something. And along the way I re-discovered the right way to write inner functions in JavaScript.

The next session had me pairing with a guy who wanted a sneak-peek into Scala (he had been a Java guy). This time we used my laptop to code. In order to give him a quick flavour of the language I decided to not use any IDE's and used a text-editor. I wanted to stay clear of ScalaTest and Scala's build system - so as to give my partner a good look at some mass of code and not distract him with externalities. We wrote code + tests in a single Scala script file. While coding I had this nagging realisation that I had not understood the problem well enough and wanted some *thinking-time* away from coding and just thinking about the puzzle. Lunch was approaching... so I decided to think hard and understand the problem better during lunchtime :-)

Post-lunch I wanted to approach the problem differently from both design and language perspective. Was delighted to find a partner who could code in Python. This guy had put in some good think-time into the problem too and the approach we came up with was very refreshing for me! He coded away in Python which I found easy to follow and we wrote a lot of code. We were quite close to solving one of the situations in the game when the time ran out. Writing so much code to solve the problem meant that the *test* code was minimal. However I was beginning to appreciate the complexity and immense freedom of design that this simple looking problem posed at us.

There were lot of Ruby developers in the group and I was very curious to get to see this language. So I chose a Ruby-man for the last session. This guy was much younger to me but surprised me by his thorough approach to TDD. He insisted on evolving the test code almost simultaneously with the main code and I must admit it initially frustrated me. I would have wanted to write a blob of test code and then write a blob of solution code and keep alternating giving good time to each. But my partner, who was the one coding in Ruby, would not have any of it. He requested that we keep shifting gears between the test-code and solution-code for every few lines of code. I knew he had a good point in what he was suggesting and asked if he could really program like this in his work time. His answer in affirmative was very convincing and I silently appreciated the young programmer's discipline.

### Epilogue
Its not everyday that I force myself to think on good programming problems and this was an excellent one. That itself made me immensely happy. I was split on approaching the problem from a *bottoms-up* or *top-down* approach - which had me thinking about an aspect of design that I had not thought for some time. I don't remember the last time doing pair programming - its a fabulous thing and want to grab every opportunity of doing so in future. The code retreat certainly pushed me away from my comfort zone and thats a great way to learn. Having not solved the problem nags me. One of these days I will sit down to write code that solves Game_Of_Life to at least some extent from few approaches that are brewing in my head. Finally I recommend Code Retreat to all. Try to get yourself a booking the next time it is happening somewhere nearby. 

Last but not the least - heard some wonderful thoughts by Venkat on the endeavour of software development. And saw a wonderful startup in Multunus at its works. The day was delight - a **Big Thank You** to all my partners and all those who made the event possible.