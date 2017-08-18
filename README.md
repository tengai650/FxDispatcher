# FxDispatcher
An event dispatcher or thread governor for worker threads sending events to the FX Application thread.
The FxDispatcher prevents worker threads from overwhelming the FX Application thread with events by governing
when events can be added to the FX Application thread.

An event cache is provided by a java.util.concurrent.BlockingQueue allowing threads to post events to the queue
and continue on. However, if the queue is full the posting thread (and any subsequent posting thread) is paused until a
queue slot becomes available. The size of the blocking queue is user configurable. 

Method waitTillPurged is designed to wait until finished, similar to SwingUtilities.invokeAndWait().

Benifits:
* All locking is done by the JDK. Low cost maintenance. 
* No additional Runnable objects created to support this interface.
* Support concurrent event posting.
* waitTillPurged method returns number of millseconds th FX Application took to purge the event queue.
* Can easily be updated to support Swing.
