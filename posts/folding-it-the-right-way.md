{{{
    "title": "Folding it the right way",
    "tags" : [ "scala", "java" ],
    "category" : "technology",
    "date" : "10-31-2013",
    "description" : ""
}}}

I have been dabbling with Scala for a few months now. And one of the things that strikes me about functional programming is the beauty of the finished code. It sometimes gives me a feeling of being just the right mix of art and science! Gone are the dirty null/empty checking *if statements*. Gone are the dumb variety *for/while loops*. I haven't progressed far enough to be using actors but the very thought that variables in my program are *not getting mutated* while being thrashed across many cores and caches is enough to sometimes give me a high!

But this blog is about something else. I just wanted to write about a small piece of code as an example of beauty, expressiveness and correctness of the functional style. I ran into this problem as part of my Scala Coursera course. First of all neither is the problem nor the solution mine. After writing a lot of imperative style ugly code to solve the problem I got fed up with myself and searched for a better way to do it. A more functional way. Here I just explain the problem and the solution.

Firstly, the problem - 

**Write a method to compute all the subsets of list of tuples. For example, given this tuple list <code>List(('a', 2), ('b', 2))</code> the list of all subsets is:**
<pre>
List(
  List(),
  List(('a', 1)),
  List(('a', 2)),
  List(('b', 1)),
  List(('a', 1), ('b', 1)),
  List(('a', 2), ('b', 1)),
  List(('b', 2)),
  List(('a', 1), ('b', 2)),
  List(('a', 2), ('b', 2))
)
</pre>

Now I request you to please try solving this. It really is not very tough. Crack up your IDE and try in the imperative style of programming. Use whatever data-structures and algorithms.

Yes, you will be able to crack it, after maybe some pain. But after you are done, give that code you wrote a hard stare. And a hard stare to the functional equivalent below. It is inevitable that you will realise, how fat our coding has grown on the unhealthy monotonous diet of pure imperative thinking all the time...

<pre>
1.	def combinations(occurrences: List[(Char, Int)]): List[List[(Char, Int)]] = 
2.		(occurrences foldRight List[List[(Char, Int)]](Nil)) 
3.		{ case ((ch,tm), acc) => 
4.    		{
5.      		acc ++ ( for { 
6.      				comb <- acc; 
7.      				n <- 1 to tm 
8.      				} yield (ch, n) :: comb 
9.      			)
10.			} 
11.		}
</pre>

So, there you have it. About 10 lines of thin code in all its glory. Now let me get under the skin of it to show what really is happening here...

First of all, Scala has the concept of tuples that helps in having cleaner data structures for problems like these. Secondly, this code (foldRight) uses currying. If you don't know about currying, that is okay. It just means that all items in a data-structure are applied on a *passed* function. The function *passed* in this case is the one that starts with the curly brace on line#3. Thirdly, this piece of code uses multiple anonymous functions.

Let me describe the execution flow step-by-step -

1. The Scala foldRight method applies the passed method on data items in the reverse. So, on passing the list <code>List(('a', 2), ('b', 2))</code>, the first data item to be used for processing is <code>('b', 2)</code>
2. foldRight takes an initial accumulator. In this case it is the <code>Nil</code> passed in line#2
3. So the initial value of parameters on line#3 are: 
	* ch = 'b'
	* tm = 2
	* acc = Nil
4. The for expression *yields* two *tuples* on being executed. The two tuples are ('b', 1) and ('b', 2). These two are appended to the Nil list and we have the result after the first pass of data structure as <code>List(List(), List((b,1)), List((b,2)))</code>
5. In the second pass, the data-item from our occurrences list being processed is <code>('a', 2)</code>. So the value of parameters this time on line#3 are:
	* ch = 'a'
	* tm = 2
	* acc = <code>List(List(), List((b,1)), List((b,2)))</code>
6. Its in this second pass, that things really get interesting. The for statement yields all 4 remaining subsets, which are <code>List(List((a,1)), List((a,2)), List((a,1), (b,1)), List((a,2), (b,1)), List((a,1), (b,2)), List((a,2), (b,2)))</code> in this single pass! It will take a little bit of mind bending to understand how this happens... but its definitely worth the effort... just reading it made my day!

Now coming to the great thing about this program - performance! Compare the number of passes on the data-structures that this piece of code has taken to the imperative code. The first thing to really digest is that this not some algorithm trickery. Now that you *know* the algorithm in *functional* programming style, try doing it the imperative style. Firstly, the code will not look this concise. Secondly, most of us will simply not be able to do it right.

But the best part - the input data structure is immutable and so are all intermediate ones. The benefit? This piece of code will **not fail if some other thread of execution changes the input variable when this piece of coding is executing**! (that is <code>List[(Char, Int)] occurrences</code> data structure). Why? Because it is impossible to change the input data structure! It is born immutable. It will live immutable. And it will die immutable. Nothing ever can come in its way!

Unfortunately, this algorithm implementation is such that output from the first iteration gets fed in the second iteration. So two parallel cores cannot be running it simultaneously. However, with all intermediate data structures being 100% immutable, it is not difficult to imagine other problems/algorithms which do not have this constraint thus using up more cores at once and built for distribution and performance! I hope you share my wow(!) about this piece of code and functional programming in this case.