package tools;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionException;

import javafx.application.Platform;

/** @Author Patrick Hoonhout
 *  @copyright 2017
 **/

public class FxDispatcher
{
    private static final BlockingQueue<Runnable> blockingQueue;
    private static final BlockingQueue<Long> blockingTillPurgedQueue;
    private static final Runnable runner;

    static
    {
	blockingQueue = new ArrayBlockingQueue<Runnable>(8); // Default queue size is 8.
	blockingTillPurgedQueue = new ArrayBlockingQueue<Long>(1);
	runner = new Runnable(){
	    @Override
	    public void run()
	    {
		try
		{
		    blockingQueue.take().run();
		}
		catch(InterruptedException e)
		{
		    return;
		}
	    } };
    }

    static public long waitTillPurged() throws InterruptedException, RejectedExecutionException
    {
	if(Platform.isFxApplicationThread()) // Can't be called by the FxApplication thread.
	    throw new RejectedExecutionException("In FxApplication thread!");
	
       	blockingQueue.put(new Runnable(){
       	    @Override
       	    public void run()
       	    {
       		try
       		{
       		    blockingTillPurgedQueue.put(Long.valueOf(System.currentTimeMillis()));
       		}
       		catch(InterruptedException e)
       		{
       		    return;
       		}
       	    } });
       	
       	long start = System.currentTimeMillis();
       	Platform.runLater(runner);
       	long stop = blockingTillPurgedQueue.take().longValue();
       	
       	return stop - start; // Total time to purge.
    }
    
    static public void postFxEvent(final Runnable run) throws InterruptedException, RejectedExecutionException
    {
	if(Platform.isFxApplicationThread()) // Can't be called by the FxApplication thread.
	    throw new RejectedExecutionException("In FxApplication thread!");
	
	blockingQueue.put(run);
	Platform.runLater(runner);
    }
}
