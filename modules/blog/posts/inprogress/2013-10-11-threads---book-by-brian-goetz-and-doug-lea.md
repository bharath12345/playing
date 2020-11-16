---
layout: post
title: "Threads - Book by Brian Goetz and Doug Lea"
category: posts
tags: []
categories: []
published: false
tweetfb: true
disqus: true
toc: true
---

1. If multiple threads access the same mutable state variable without appropriate synchronization, your program is broken. There are three ways to fix it: 		* Don't share the state variable across threads; 
	* Make the state variable immutable; or 	* Use synchronization whenever accessing the state variable. 
2. What is thread-safety?
	
	A  class is threadsafe if it behaves correctly when accessed from multiple threads, regardless of the scheduling or interleaving of the execution of those threads by the runtime environment, and with no additional synchronization or other coordination on the part of the calling code.
	
3. Where practical, use existing threadsafe objects, like AtomicLong, to manage your class's state. It is simpler to reason  about the possible states and state transitions for existing threadsafe objects than it is for arbitrary state variables, and  this makes it easier to maintain and verify thread safety.

4. To preserve state consistency, update related state variables in a single atomic operation. 

5. ￼Stateless and immutable objects are always thread safe. 6. For each  mutable  state  variable  that  may  be  accessed  by  more  than  one  thread,  all  accesses  to  that  variable  must  be  performed with the same lock held. In this case, we say that the variable is guarded by that lock. 
7. In  the  absence  of  synchronization,  the  compiler,  processor,  and  runtime  can  do  some  downright  weird  things  to  the  order  in  which  operations  appear  to  execute.  Attempts  to  reason  about  the  order  in  which  memory  actions  "must"  happen in insufficiently synchronized multithreaded programs will almost certainly be incorrect.
8. Synchronizing  only  the  setter  would  not  be  sufficient
9. In the JVM is permitted to treat a 64-bit read or write of long/double as two separate 32-bit operations. If the reads and writes occur in different threads, it is therefore possible to read a nonvolatile long and get back the high 32 bits of one value and the low 32 bits of another.[3] Thus, even if you don't care about stale values, it is  not  safe  to  use  shared  mutable  long  and  double  variables  in  multithreaded  programs  unless  they  are  declared  volatile or guarded by a lock.
10. But reference assignment and modification is always atomic… even in 64-bit systems
11. Volatile  variables  are  not  cached  in  registers  or  in  caches  where  they  are  hidden  from  other  processors, so a read of a volatile variable always returns the most recent write by any thread. 
12. Why is it wrong to start a thread from a constructor?

	When an object creates a thread from its constructor, it almost always shares its this reference with the new thread,  either explicitly (by passing it to the constructor) or implicitly (because the Thread or Runnable is an inner class of the  owning  object).  The  new  thread  might  then  be  able  to  see  the  owning  object  before  it  is  fully  constructed.  There's  nothing wrong with creating a thread in a constructor, but it is best not to start the thread immediately. Instead, expose  a  start  or  initialize  method  that  starts  the  owned  thread.
	
13. What is ThreadLocal?

	A more formal means of maintaining thread confinement is ThreadLocal, which allows you to associate a per thread  value with a value holding object. Thread-Local provides get and set accessor methods that maintain a separate copy  of  the  value  for  each  thread  that  uses  it,  so  a  get  returns  the  most  recent  value  passed  to  set  from  the  currently  executing thread. 
14. 