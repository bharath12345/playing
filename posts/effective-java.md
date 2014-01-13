{{{
    "title": "Effective Java",
    "subheading": "",
    "tags" : [ "book", "java" ],
    "category" : "technology",
    "date" : "08-22-2013",
    "description" : ""
}}}

I read this beautifully written article a few days ago - "[I will not do your tech interview](https://medium.com/lessons-learned/80ba19c55883)". I can't agree more with the author. Every single time I have had to give/take a technical interview, more than the sense of being inadequately prepared I feel like carrying an inexplicable psychological burden. And I have met no one who does not fear what Ellis beautifully calls as - *"bear-trap of a stupid brainteaser"* :-).

In the years to come, internet is definitely going to give more and more relief to competent engineers. Having a GitHub repository with a dump of one's pet technology prototypes, having a StackOverflow point score, well articulated tweets and maybe even well-written technology blog (read [this](http://nathanmarz.com/blog/break-into-silicon-valley-with-a-blog-1.html) by Nathan Marz) will pay dividends to engineers continuously at work to sharpen their axe…

But then, my current reality is a reality. And I have to take technical interviews as part of my job. And hiring the right people is so much more important for a small company - many times it is the only differentiator between success and failure of the company itself. So with the job's being dished out being so important, technical interviews are not supposed to be easy. Both for the interviewee and the interviewer. Pressed into the interviewing job, I felt the need to brush-up my fundamentals. This post is from my re-read of Joshua Bloch's classic - "[Effective Java](http://www.amazon.com/Effective-Java-Edition-Joshua-Bloch/dp/0321356683)" - from a interviewer's perspective… trying to quickly refresh the elementary concepts to myself. It aint coherent or complete… will keep adding stuff to this post over time as I realise what questions really make the cut. There are plenty of *interview-questions* blogs and books out there - but I felt, instead of quizzing a candidate on some corner case of the JVM or language (which many times the interviewer himself might have realised just hours before the interview), it would be more honest/ethical on my part to quiz in what are well-known and real-world areas of programming for an aspiring engineer - and 'Effective Java' is precisely the guide for such a setting…

Now planning to write few more blogs like these in the days to come… one surely on Design Patterns by GoF. Maybe one on JavaScript's good parts per Doughlas Crockford. And time permitting, few more… 

#### Creating and Destorying Objects
<div class="bs-docs-grid">
    <div class="row show-grid">
        <div class="col-md-2 left">Consider static factory methods instead of constructors</div>
        <div class="col-md-10 right">Similar to flyweight. valueof/of/getInstance/newInstance/getType/newType</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-2 left">Consider a builder when faced with many constructor parameters</div>
        <div class="col-md-10 right">Telescoping constructors are hard to read and write. Inconsistent state partway through the construction</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-2 left">Enforce the singleton property with a private constructor or an enum type</div>
        <div class="col-md-10 right">All instance fields should be transient. Provide a readResolve() method else serialization/deserialization can lead to new objects</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-12">Enforce non-instantiability with a private constructor</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-2 right">Avoid creating unnecessary objects</div>
        <div class="col-md-10 left">A statement like this in a for loop can lead to huge number of unnecessary objects getting created -
            <code>String s = new String("stringette");</code>
            The improved version is simply the following:
            <code>String s = "stringette";</code>
            This version uses a single String instance, rather than creating a new one each time it is executed. Furthermore, it is guaranteed that the object will be reused by any other code running in the same virtual machine that happens to con- tain the same string literal
            The static factory method <code>Boolean.valueOf(String)</code> is almost always preferable to the constructor <code>Boolean(String)</code></div>
    </div>
    <div class="row show-grid">
        <div class="col-md-2 right">Eliminate obsolete object references</div>
        <div class="col-md-10 left">Spot the memory leak in this program?
            <pre>
            public class Stack {
                   private Object[] elements;
                   private int size = 0;
                   private static final int DEFAULT_INITIAL_CAPACITY = 16;
                   public Stack() {
                       elements = new Object[DEFAULT_INITIAL_CAPACITY];
                   }
                   public void push(Object e) {
                       ensureCapacity();
                       elements[size++] = e;
                   }
                   public Object pop() {
                       if (size == 0)
                           throw new EmptyStackException();
                       return elements[--size];
                   }
                   /**
                    * Ensure space for at least one more element, roughly
                    * doubling the capacity each time the array needs to grow.
                    */
                   private void ensureCapacity() {
                       if (elements.length == size)
                           elements = Arrays.copyOf(elements, 2 * size + 1);
                   }
            }</pre>
        </div>
    </div>
    <div class="row show-grid">
        <div class="col-md-2 left">Avoid finalizers</div>
        <div class="col-md-10 right">What is a finalizer? Is it always called by the GC? Is there a performance penalty to using finalizer? Why?</div>
    </div>
</div>

#### The Java methods common to all objects
<div class="bs-docs-grid">
    <div class="row show-grid">
        <div class="col-md-2 right">Obey the general contract when overriding equals()</div>
        <div class="col-md-10 left">
            <h5>1. When do you override equals()?</h5>
            When a class has a notion of logical equality that differs from mere object identity, and a superclass has not already overridden equals to implement the desired behavior.
            <h5>2. What are the main rules that you would follow to implement equals()?</h5>
            <ul class="list-group">
                <li class="list-group-item">Use == to check for same reference</li>
                <li class="list-group-item">Use instanceof to check if the agrument is of the correct type</li>
                <li class="list-group-item">Match all significant fields of the two objects</li>
                <li class="list-group-item">Symmetric? Transivitve? Consistent?</li>
                <li class="list-group-item">override hashCode()</li>
            </ul>
        </div>
    </div>
    <div class="row show-grid">
        <div class="col-md-2 right">Always override hashCode() when you override equals()</div>
        <div class="col-md-10 left">
            <h6>1. If two objects are equal according to the equals (Object) method, then calling the hashCode method on each of the two objects must produce the same integer result.</h6>
            <h6>2. It is not required that if two objects are unequal according to the equals (Object) method, then calling the hashCode method on each of the two objects must produce distinct integer results. However, the programmer should be aware that producing distinct integer results for unequal objects may improve the performance of hash tables.</h6>
            <h6>3. How will you compute the hashCode()? Do not be tempted to exclude significant parts of an object from the hash code computation to improve performance</h6>
        </div>
    </div>
    <div class="row show-grid">
        <div class="col-md-12">Always override toString()</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-2 right">Override clone() judiciously</div>
        <div class="col-md-10 left">
        <h5>1. Does Cloneable interface have a clone() method? Why not?</h5>
        Because the Java Object's clone() method (which is protected) is supposed to be used
        <h5>2. How does Java Object's clone() method work?</h5>
        If a class implements Cloneable, Object’s clone method returns a field-by-field copy of the object; otherwise it throws CloneNotSupportedException
        <h5>3. What are the 3 rules for implementing Cloneable?</h5>
            a. x.clone() != x
            b. x.clone().getClass() == x.getClass()
            c. x.clone().equals(x)
        <h5>4. How to clone properly?</h5>
        All classes that implement Cloneable should override clone with a public method whose return type is the class itself. This method should first call super.clone and then fix any fields that need to be fixed. Typically, this means copying any mutable objects that comprise the internal “deep structure” of the object being cloned, and replacing the clone’s references to these objects with ref- erences to the copies. While these internal copies can generally be made by call- ing clone recursively, this is not always the best approach. If the class contains only primitive fields or references to immutable objects, then it is probably the case that no fields need to be fixed.
        <h5>5. How come interfaces like Cloneable and Serializable have no methods? Why do they exist at all then? How does JVM use them?</h5>
        The UID and custom readers/writers are accessed via reflection.
        Serializable serves as a marker to the JRE/JVM, which may take action(s) based on its presence. Refer to http://en.wikipedia.org/wiki/Marker_interface_pattern. An example of the application of marker interfaces from the Java programming language is the Serializable interface. A class implements this interface to indicate that its non-transient data members can be written to an ObjectOutputStream. The ObjectOutputStream private method writeObject() contains a series of instanceof tests to determine writeability, one of which looks for the Serializable interface. If any of these tests fails, the method throws a NotSerializableException.
        </div>
    </div>
    <div class="row show-grid">
        <div class="col-md-2 right">Consider implementing Comparable</div>
        <div class="col-md-10 left">
            <h5>1. What is the use of the Comparable interface?</h5>
        Helps in sorting when there is a natural order among the objects
            <h5>2. Whats the difference between interfaces like Comparable and those like Cloneable/Serializable?</h5>
        </div>
    </div>
</div>

#### Classes and Interfaces
<div class="bs-docs-grid">
    <div class="row show-grid">
        <div class="col-md-2 right">Minimize the accessibility of classes and members</div>
        <div class="col-md-10 left">What is package-private? How do you implement?
        The member is accessible from any class in the package where it is declared. Technically known as default access, this is the access lev- el you get if no access modifier is specified.</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-12">In public classes, use public classes not public fields</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-2 right">Minimize mutability</div>
        <div class="col-md-10 left">
<h5>1. Is it good or bad to minimize mutability? why?</h5>
     If objects are immutable they are automatically thread-safe and no synchronization or locking is required
<h5>2. How would you make an object immutable?</h5>
<ul class="list-group">
    <li class="list-group-item">No mutators - no setters</li>
    <li class="list-group-item">Class cant be extended - class should be marked final</li>
    <li class="list-group-item">Make all fields final</li>
    <li class="list-group-item">Make all fields private</li>
    <li class="list-group-item">Ensure exclusive access to any mutable components</li>
    <li class="list-group-item">getters should return a new instance of the object</li>
</ul>
</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-12">Favor composition over inheritance</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-12">Design and document for inheritance or else prohibit it</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-2 right">Prefer interfaces to abstract classes</div>
        <div class="col-md-10 left">
            <h5>Why are interfaces better than abstract classes?</h5>
            <ul class="list-group">
                <li class="list-group-item">Existing classes can be easily retrofitted to implement a new interface</li>
                <li class="list-group-item">Interfaces are ideal for defining mixins</li>
                <li class="list-group-item">Interfaces allow the construction of nonhierarchical type frameworks.</li>
                <li class="list-group-item">Interfaces enable safe, powerful functionality enhancements</li>
                <li class="list-group-item">combine the virtues of interfaces and abstract classes by providing an abstract skeletal implementation class to go with each nontrivial interface that you export</li>
            </ul>
        </div>
    </div>
    <div class="row show-grid">
        <div class="col-md-2 right">Use interfaces only to define types</div>
        <div class="col-md-10 left">
            <h5>Is 'constants' in an interface a good programming pattern?</h5>
            No.
        </div>
    </div>
    <div class="row show-grid">
        <div class="col-md-12">Prefer class hierarchies to tagged classes</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-12">Use function objects to represent strategies</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-2 right">Favor static member classes over nonstatic</div>
        <div class="col-md-10 left">
<h5>1. What are the 4 kinds of nested classes?</h5>
            <ul class="list-group">
                <li class="list-group-item">a. static member classes</li>
                <li class="list-group-item">b. nonstatic member classes</li>
                <li class="list-group-item">c. anonymous classes</li>
                <li class="list-group-item">d. local classes</li>
            </ul>

<h5>2. When will you make a nested class static?</h5>
If an instance of a nested class can exist in isolation from an instance of its enclosing class, then the nested class must be a static member class: it is impossible to create an instance of a nonstatic member class without an enclosing instance. If you declare a member class that does not require access to an enclosing instance, always put the static modifier in its declaration

<h5>3. Why would one prefer static classes?</h5>
The association between a nonstatic member class instance and its enclosing instance is established when the former is created; it cannot be modified thereafter. Storing this reference costs time and space, and can result in the enclosing instance being retained when it would otherwise be eligible for garbage collection
        </div>
    </div>
</div>

#### Generics
<div class="bs-docs-grid">
    <div class="row show-grid">
        <div class="col-md-2 right">Dont use raw types in new code</div>
        <div class="col-md-10 left">
<h5>1. What is the problem with doing <code>private final Collection stamps = ... ;</code></h5>
        Loss of compile time type safety
<h5>2. Is List<String>.class legal? What will it give me?</h5>
        It is not legal
        </div>
    </div>
    <div class="row show-grid">
        <div class="col-md-2 right">Eliminate unchecked warnings</div>
        <div class="col-md-10 left">
<h5>How do you eliminate a unchecked warning?</h5>
        Suppress the warning with an @SuppressWarnings("unchecked") annotation. Always use the Suppress- Warnings annotation on the smallest scope possible.
        </div>
    </div>
    <div class="row show-grid">
        <div class="col-md-2 right">Prefer lists to arrays</div>
        <div class="col-md-10 left">
<h5>1. If Sub is a subtype of Super, then is the array Sub[] a subtype of Super[]?</h5>
Yes. Arrays are covariant. Lists are invariant
<h5>2. So which one is better? And why?</h5>
Lists are better. Arrays are reified. This means that arrays know and enforce their element types at runtime. Generics, by contrast, are implemented by erasure. This means that they enforce their type constraints only at compile time and discard (or erase) their element type information at runtime.
<h5>3. Test question -</h5>
This code fragment is legal but fails at runtime! -
                  <pre>
                  Object[] objectArray = new Long[1];
                  objectArray[0] = "I don't fit in"; // Throws ArrayStoreException</pre>
                  But this one wont compile at all! -
                  <pre>List&lt;Object&gt; ol = new ArrayList&lt;Long&gt;(); // Incompatible types ol.add("I don't fit in");</pre>
<h5>4. Are these legal?</h5>
<pre>new List&lt;E&gt;[]
new List&lt;String&gt;[]
new E[]</pre>
No. It is illegal to create an array of a generic type, a parameterized type, or a type parameter. Types such as E, List&lt;E&gt;, and List<String> are technically known as non-reifiable types. Intuitively speaking, a non-reifiable type is one whose runtime representation contains less information than its compile-time representation.
        </div>
    </div>
    <div class="row show-grid">
        <div class="col-md-2 right">Favor generic types</div>
        <div class="col-md-10 left">Which of these is better and why?
        <pre>
        public class Stack {
            private Object[] elements;
            public void push(Object e) {
            }
            public Object pop() {
            }
        }</pre>
        or
        <pre>
        public class Stack<E> {
            private E[] elements;
            public void push(E e) {
            }
            public E pop() {
            }
        }</pre>
        </div>
    </div>
    <div class="row show-grid">
        <div class="col-md-2 right">Favor generic methods</div>
        <div class="col-md-10 left">Which of these is better and why?
        <code>public static Set union(Set s1, Set s2)</code>
        or
        <code>public static &lt;E&gt; Set&lt;E&gt; union(Set&lt;E&gt; s1, Set&lt;E&gt; s2)</code>
        </div>
    </div>
    <div class="row show-grid">
        <div class="col-md-2 right">Use bounded wildcards to increase API flexibility</div>
        <div class="col-md-10 left">What is the PECS rule or Get-and-Put principle?
        Bounded wildcards can be of two types -
        <code>X&lt;? extends E&gt;</code>
        or
        <code>Y&lt;? super E&gt;</code>
        PECS stands for producer-extends, consumer-super.
        In other words, if a parameterized type represents a T producer, use <? extends T>; if it represents a T consumer, use <? super T>.
        </div>
    </div>
    <div class="row show-grid">
        <div class="col-md-12">Consider typesafe heterogenous containers</div>
    </div>
</div>

#### Enums and Annotations
<div class="bs-docs-grid">
    <div class="row show-grid">
        <div class="col-md-2 right">Use enums instead of int constants</div>
        <div class="col-md-10 left">
<h5>1. Does enum extend Java Object?</h5>
They provide high-quality implementations of all the Object methods

<h5>2. Which interfaces do enum implement?</h5>
they implement Comparable and Serializable, and their serialized form is designed to withstand most changes to the enum type.

<h5>3. How would you associate data with enums?</h5>
To associate data with enum constants, declare instance fields and write a constructor that takes the data and stores it in the fields. Enums are by their nature immutable, so all fields should be final

<h5>4. How would you associate a different behavior with every enum constant?</h5>
using apply()

</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-2 left">Use instance fields instead of ordinals</div>
        <div class="col-md-10 right">
<h5>Is using ordinals a bad idea? If so, what is the option?</h5>
        Use instance fields
        </div>
    </div>
    <div class="row show-grid">
        <div class="col-md-2 right">Use EnumSet instead of bit fields</div>
        <div class="col-md-10 left">Whats the usecase for EnumSets?
        Instead of bit fields which look ugly like this <code>text.applyStyles(STYLE_BOLD | STYLE_ITALIC);</code>
        one can do this -
        <code>text.applyStyles(EnumSet.of(Style.BOLD, Style.ITALIC));</code>
        </div>
    </div>
    <div class="row show-grid">
        <div class="col-md-2 left">Use EnumMap instead of ordinal indexing</div>
        <div class="col-md-10 right">It is rarely appropriate to use ordinals to index arrays: use EnumMap instead</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-12">Emulate extensible enums with interfaces</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-2 right">Prefer annotations to naming patterns</div>
        <div class="col-md-10 left">
<h5>1. Any usecase you can think of for custom annotations?</h5>
JUnit testing framework originally required its users to designate test methods by beginning their names with the characters test
<h5>2. Which annotation do you use most?</h5>
@Override, @Deprecated, @SuppressWarnings
        </div>
    </div>
    <div class="row show-grid">
        <div class="col-md-2 right">Consistently use the Override annotation</div>
        <div class="col-md-10 left">
<h5>What @Override for?</h5>
        it indicates that the annotated method declaration overrides a declaration in a supertype
        </div>
    </div>
    <div class="row show-grid">
        <div class="col-md-12">Use marker interfaces to define types</div>
    </div>
</div>

#### Methods
<div class="bs-docs-grid">
    <div class="row show-grid">
        <div class="col-md-12">Check parameters for validity</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-12">Make defensive copies when needed</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-2 right">Design method signatures carefully</div>
        <div class="col-md-10 left">
<h5>Is Map as a method parameter better or HashMap - why?</h5>
        Map is. This is super basic.
        </div>
    </div>
    <div class="row show-grid">
        <div class="col-md-12">Use overloading judiciously</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-12">Use varargs judiciously</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-2 right">Return empty arrays or collections, not nulls</div>
        <div class="col-md-10 left">
<h5>What is better - returning null or empty collections?</h5>
        Empty Collections
        </div>
    </div>
    <div class="row show-grid">
        <div class="col-md-12">Write doc comments for all exposed API comments</div>
    </div>
</div>

#### General Programming
<div class="bs-docs-grid">
    <div class="row show-grid">
        <div class="col-md-12">Minimize the scope of local variables</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-12">Prefer foreach loops to traditional for loops</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-12">Know and use the libraries</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-12">Avoid float and double if exact answers are required</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-2 right">Prefer primitives to boxed primitives</div>
        <div class="col-md-10 left">
<h5>What makes the performance of this program bad?</h5>
            <pre>
            public static void main(String[] args) {
                Long sum = 0L;
                for (long i = 0; i < Integer.MAX_VALUE; i++) {
                    sum += i;
                }
                System.out.println(sum);
            }</pre>
        </div>
    </div>
    <div class="row show-grid">
        <div class="col-md-12">Avoid strings when other types are more appropriate</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-2 right">Beware the performance of string concatenation</div>
        <div class="col-md-10 left">
<h5>1. Before 1.5, for string concatenation StringBuffer was preferred - what is it now?</h5>
        StringBuilder
<h5>2. What is the difference between StringBuilder and StringBuffer?</h5>
        StringBuider is unsynchronized - this makes it much faster. But should be used with care in concurrent programs
        </div>
    </div>
    <div class="row show-grid">
        <div class="col-md-2 right">Refer to objects by their interfaces</div>
        <div class="col-md-10 left">
<h5>Which one is better and why?</h5>
        <pre>
        List<Subscriber> subscribers = new ArrayList<Subscriber>();
        ArrayList<Subscriber> subscribers = new ArrayList<Subscriber>();</pre>
        </div>
    </div>
    <div class="row show-grid">
        <div class="col-md-2 right">Prefer interfaces to reflection</div>
        <div class="col-md-10 left">
<h5>Reflection allows one class to use another, even if the latter class did not exist when the former was compiled. So what are the problems using it?</h5>
    <ul class="list-group">
        <li class="list-group-item">You lose all the benefits of compile-time type checking, including exception checking</li>
        <li class="list-group-item">The code required to perform reflective access is clumsy and verbose</li>
        <li class="list-group-item">Performance suffers</li>
    </ul>
        </div>
    </div>
    <div class="row show-grid">
        <div class="col-md-12">Use native methods judiciously</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-12">Optimize judiciously</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-12">Adhere to generally accepted naming conventions</div>
    </div>
</div>

#### Exceptions
<div class="bs-docs-grid">
    <div class="row show-grid">
        <div class="col-md-12">Use exceptions only for exceptional conditions</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-2 right">Use checked exceptions for recoverable conditions and runtime exceptions for programming errors</div>
        <div class="col-md-10 left">
<h5>1. What are the different types of exceptions?</h5>
        * Checked exceptions
        * Unchecked exceptions - runtime exceptions and errors
<h5>2. When would you code for checked exceptions?<h5>
        when the caller is can reasonably expected to recover
<h5>3. When would you throw a runtime exception?</h5>
        When the program is as good as dead
<h5>4. When would you throw a error?</h5>
         there is a strong convention that errors are reserved for use by the JVM to indicate resource defi- ciencies, invariant failures, or other conditions that make it impossible to continue execution. Given the almost universal acceptance of this convention, it’s best not to implement any new Error subclasses. Therefore, all of the unchecked throw- ables you implement should subclass RuntimeException
        </div>
    </div>
    <div class="row show-grid">
        <div class="col-md-2 right">Avoid unnecessary use of checked exceptions</div>
        <div class="col-md-10 left">
<h5>Tell me the exceptions you know and when you would use them</h5>
<ul class="list-group">
    <li class="list-group-item">IllegalArgumentException - argument aint right</li>
    <li class="list-group-item">IllegalStateException - calling a method on an object before it is properly initialized</li>
    <li class="list-group-item">NullPointerException - someone invokes a method on a null object</li>
    <li class="list-group-item">ConcurrentModificationException - if a object designed to be used by a single thread is being concurrently modified</li>
    <li class="list-group-item">IndexOutOfBoundException - accessing array beyond its data length</li>
    <li class="list-group-item">UnsupportedOperationException - object does not support a method</li>
</ul>
        </div>
    </div>
    <div class="row show-grid">
        <div class="col-md-12">Favor the use of standard exceptions</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-12">Throw exceptions appropriate to the abstraction</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-12">Document all exceptions thrown by each method</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-12">Include failure-capture information in detail messages</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-12">Strive for failure atomicity</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-12">Dont ignore exceptions</div>
    </div>
</div>

#### Concurrency
<div class="bs-docs-grid">
    <div class="row show-grid">
        <div class="col-md-2 right">Synchronize access to shared mutable data</div>
        <div class="col-md-10 left">
<h5>1. Is writing of all primitive data types atomic in Java?</h5>
        reading or writing a variable is atomic unless the variable is of type long or double
<h5>2. How long would you expect this program to run?</h5>
        <pre>
        public class StopThread {
               private static boolean stopRequested;
               public static void main(String[] args)
                       throws InterruptedException {
                   Thread backgroundThread = new Thread(new Runnable() {
                       public void run() {
                           int i = 0;
                           while (!stopRequested)
                                i++;
                       }
                   });
                   backgroundThread.start();
                   TimeUnit.SECONDS.sleep(1);
                   stopRequested = true;
               }
        }</pre>
        Probably permanently. The VM might do what is called hoisting, the virtual machine might transform this code:
        <pre>
        while (!done)
            i++;</pre>
        into this code:
        <pre>
        if (!done)
            while (true)
                i++;</pre>
        How would you correct his?

<h5>3. Is this program thread safe? Can generateSerialNumber() be called from multiple threads safely?</h5>
        <pre>
        private static volatile int nextSerialNumber = 0;
           public static int generateSerialNumber() {
               return nextSerialNumber++;
        }</pre>

<h5>4. What are the 4 factors that need trade-off when writing multi-threaded concurrent programs?</h5>
        Safety, Liveness, Efficiency, Reusability

<h5>5. Whats the tradeoff between Safety and Liveness?</h5>
        safety: nothing bad happens
        liveness: something good eventually happens

<h5>6. What is reentracy? Is Java reentrant?</h5>
        Yes

<h5>7. Whats the difference between ArrayList and CopyOnWriteArrayList?</h5>
        It is a variant of ArrayList in which all write operations are implemented by making a fresh copy of the entire underlying array. Because the internal array is never modified, iteration requires no locking and is very fast. For most uses, the performance of CopyOnWriteArrayList would be atrocious, but it’s perfect for observer lists, which are rarely modified and often traversed.
        </div>
    </div>
    <div class="row show-grid">
        <div class="col-md-12">Avoid excessive synchronization</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-2 right">Prefer executors and tasks to threads</div>
        <div class="col-md-10 left">
<h5>1. In the post Java 1.5 world, the use of 'Thread' is probably not a good idea due to the availability of new functionality in java.util.concurrent - what are they?</h5>
                Executors and tasks
 <h5>2. There are some data structures designed in Java collections specifically for concurrent usage - what are they and how do they work?</h5>
         ConcurrentHashMap etc
<h5>3. Why is it a bad idea to rely on Thread.yield or Java's thread priorities API?</h5>
        Not portable
      </div>
    </div>
    <div class="row show-grid">
        <div class="col-md-12">Prefer concurrency utilities to wait and notify</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-12">Document thread safety</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-12">Use lazy initialization judiciously</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-12">Dont depend on the thread scheduler</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-12">Avoid thread groups</div>
    </div>
</div>

#### Serialization
<div class="bs-docs-grid">
    <div class="row show-grid">
        <div class="col-md-2 right">Implement serializable judiciously</div>
        <div class="col-md-10 left">
<h5>1. What is serialVersionUID?</h5>
 Every serializable class has a unique identification number associated with it. If you do not specify this number explicitly by declaring a static final long field named serialVersionUID, the system automatically generates it at runtime by applying a complex procedure to the class. The automatically generated value is affected by the class’s name, the names of the interfaces it implements, and all of its public and protected members. If you change any of these things in any way, for example, by adding a trivial convenience method, the automatically generated serial version UID changes. If you fail to declare an explicit serial version UID, compatibility will be broken, resulting in an InvalidClassException at runtime. If no serial version UID is provided, an expensive computation is required to generate one at runtime. If you ever want to make a new version of a class that is incompatible with existing versions, merely change the value in the serial version UID declaration.

<h5>2. Why should a class be made to implement Serilizable with caution?</h5>
 A major cost of implementing Serializable is that it decreases the flexibility to change a class’s implementation once it has been released.
        </div>
    </div>
    <div class="row show-grid">
        <div class="col-md-2 right">Consider using a custom serialized form</div>
        <div class="col-md-10 left">
<h5>How good is Java's ObjectStream based Serialization? When would you implement your own custom serialized form?</h5>
        The default serialized form of an object is a reasonably efficient encoding. The default serialized form is likely to be appropriate if an object’s phys- ical representation is identical to its logical content. Drawbacks - can be excessive in space consumption, not very fast, it permanently ties the exported API to the current internal representation
        </div>
    </div>
    <div class="row show-grid">
        <div class="col-md-12">Write readObject methods defensively</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-12">For instance control prefer enum types than readResolve</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-12">Consider serialization proxies instead of serialized instances</div>
    </div>
</div>


#### End Node
