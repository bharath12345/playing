---
layout: post
title: "Design Patterns"
category: posts
tags: []
categories: []
published: false
tweetfb: true
disqus: true
---

This is one article I have been wanting to write for long. In the world of *Design Patterns* for *Enterprise Applications* there are four texts (names below) that are of massive importance. They have contributed immensely to the vocabulary of software engineering and product development. The four texts being -

* **[Design Patterns: Elements Of Reusable Object-Oriented Software](http://www.amazon.com/Design-Patterns-Elements-Reusable-Object-Oriented/dp/0201633612)** by *The Gang of Four*, Addison Wesley, 1994
* **[Patterns Of Enterprise Application Architecture](http://www.amazon.com/Patterns-Enterprise-Application-Architecture-Martin/dp/0321127420)** by *Martin Fowler*, Addison Wesley, 2002
* **[Core J2EE Design Patterns](http://)** by *Deepak Alur et al*, 
* **[Enterprise Integration Patterns](http://www.amazon.com/Enterprise-Integration-Patterns-Designing-Deploying/dp/0321200683)** by *Gregor Hohpe and Bobby Woolf*, Addison Wesley, 2003

Why do I write? A quick round-up of these patterns in a tabular form helps in two ways - 

* Quick refresher to myself whenever my memory needs a jog
* Writing helps ingest the vocabulary of patterns more thoroughly 

### Creational Patterns
<table class="table table-bordered table-striped table-condensed bs-docs-grid">
    <tr class="tablerow"">
        <td>Abstract Factory</td>
        <td>Provide an interface for creating families of related or dependent objects without specifying their concrete classes. **Based on Composition**</td>
        <td></td>
    </tr>
    <tr class="tablerow"">
        <td>Builder</td>
        <td>Separate the construction of a complex object from its representation so that the same construction process can create different representations.**Based on Composition**</td>
        <td></td>
    </tr>
    <tr class="tablerow"">
        <td>Factory Method</td>
        <td>Define an interface for creating an object, but let subclasses decide which class to instantiate. Factory Method lets a class defer instantiation to subclasses. **Based on Subclassing**</td>
        <td></td>
    </tr>
    <tr class="tablerow"">
        <td>Prototype</td>
        <td>Specify the kinds of objects to create using a prototypical instance, and create new objects by copying this prototype.**Based on Composition**</td>
        <td></td>
    </tr>
    <tr class="tablerow"">
        <td>Singleton</td>
        <td>Ensure a class only has one instance, and provide a global point of access to it.</td>
        <td></td>
    </tr>
</table>

### Structural Patterns
<table class="table table-bordered table-striped table-condensed bs-docs-grid">
    <tr class="tablerow"">
        <td>Adapter</td>
        <td>Convert the interface of a class into another interface clients expect. Adapter lets classes work together that couldn't otherwise because of incompatible interfaces.</td>
        <td></td>
    </tr>
    <tr class="tablerow"">
        <td>Bridge</td>
        <td>Decouple an abstraction from its implementation so that the two can vary independently.</td>
        <td></td>
    </tr>
    <tr class="tablerow"">
        <td>Composite</td>
        <td>Compose objects into tree structures to represent part-whole hierarchies. Composite lets clients treat individual objects and compositions of objects uniformly.</td>
        <td></td>
    </tr>
    <tr class="tablerow"">
        <td>Decorator</td>
        <td>Attach additional responsibilities to an object dynamically. Decorators provide a flexible alternative to subclassing for extending functionality.</td>
        <td></td>
    </tr>
    <tr class="tablerow"">
        <td>Facade</td>
        <td>Provide a unified interface to a set of interfaces in a subsystem. Facade defines a higher-level interface that makes the subsystem easier to use.</td>
        <td></td>
    </tr>
    <tr class="tablerow"">
        <td>Flyweight</td>
        <td>Use sharing to support large numbers of fine-grained objects efficiently.</td>
        <td></td>
    </tr>
    <tr class="tablerow"">
        <td>Proxy</td>
        <td>Provide a surrogate or placeholder for another object to control access to it.</td>
        <td></td>
    </tr>
</table>

### Behavioral Patterns
<table class="table table-bordered table-striped table-condensed bs-docs-grid">
    <tr class="tablerow"">
        <td>Chain of responsibility</td>
        <td>Avoid coupling the sender of a request to its receiver by giving more than one object a chance to handle the request. Chain the receiving objects and pass the request along the chain until an object handles it.</td>
        <td></td>
    </tr>
    <tr class="tablerow"">
        <td>Command</td>
        <td>Encapsulate a request as an object, thereby letting you parameterizeclients with different requests, queue or log requests, and supportundoable operations.</td>
        <td>HttpRequest, UI events</td>
    </tr>
    <tr class="tablerow"">
        <td>Interpreter</td>
        <td>Given a language, define a represention for its grammar along with aninterpreter that uses the representation to interpret sentences in thelanguage</td>
        <td>RegEx</td>
    </tr>
    <tr class="tablerow"">
        <td>Iterator</td>
        <td>Provide a way to access the elements of an aggregate objectsequentially without exposing its underlying representation.</td>
        <td></td>
    </tr>
    <tr class="tablerow"">
        <td>Mediator</td>
        <td>Define an object that encapsulates how a set of objects interact.Mediator promotes loose coupling by keeping objects from referring to each other explicitly, and it lets you vary their interaction independently.</td>
        <td>Set of interconnected widgets on a HTML form</td>
    </tr>
    <tr class="tablerow"">
        <td>Memento</td>
        <td>Without violating encapsulation, capture and externalize an object's internal state so that the object can be restored to this state later.</td>
        <td></td>
    </tr>
    <tr class="tablerow"">
        <td>Observer</td>
        <td>Define a one-to-many dependency between objects so that when one object changes state, all its dependents are notified and updated automatically.</td>
        <td></td>
    </tr>
    <tr class="tablerow"">
        <td>State</td>
        <td>Allow an object to alter its behavior when its internal state changes.The object will appear to change its class.</td>
        <td></td>
    </tr>
    <tr class="tablerow"">
        <td>Strategy</td>
        <td>Define a family of algorithms, encapsulate each one, and make them interchangeable. Strategy lets the algorithm vary independently from clients that use it.</td>
        <td></td>
    </tr>
    <tr class="tablerow"">
        <td>Template Method</td>
        <td>Define the skeleton of an algorithm in an operation, deferring some steps to subclasses. Template Method lets subclasses redefine certain steps of an algorithm without changing the algorithm's structure.</td>
        <td></td>
    </tr>
    <tr class="tablerow"">
        <td>Visitor</td>
        <td>Represent an operation to be performed on the elements of an object structure. Visitor lets you define a new operation without changing the classes of the elements on which it operates.</td>
        <td></td>
    </tr>
</table>
