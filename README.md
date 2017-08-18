# FxDispatcher
Event dispatch or thread governer for worker threads sending events to the FX application thread.
This prevents worker threads from overwhelming the FX Application thread with events. 

An event cache is provide by a BlockingQueue allowing threads to post events and continue on. 
However, if the queue is full the posting thread is paused until a queue slot becomes available.
The size of the queue is user configurable. 

Method waitTillPurged is designed to wait until finished, similar to SwingUtilities.invokeAndWait().


Benifits:
* All locking is done by the JDK. Low cost maintenance. 
* No additional Runnable objects created to support this interface.
* Support concurrent event posting.
* waitTillPurged method returns number of millseconds th FX Application took to purge the event queue.
* Can easily be updated to support Swing.
