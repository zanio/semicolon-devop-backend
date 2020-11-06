package com.semicolondevop.suite.util.helper;

/**
 * @author Aniefiok on 8/5/2020
 * @project IntelliJ IDEA
 */

public class RetryOnException {
    public static final int DEFAULT_RETRIES = 10;
    public static final long DEFAULT_TIME_TO_WAIT_MS = 5000;

    private int numRetries;
    private long timeToWaitMS;

    // CONSTRUCTORS
    public RetryOnException(int _numRetries,
                            long _timeToWaitMS)
    {
        numRetries = _numRetries;
        timeToWaitMS = _timeToWaitMS;
    }

    public RetryOnException()
    {
        this(DEFAULT_RETRIES, DEFAULT_TIME_TO_WAIT_MS);
    }

    /**
     * shouldRetry
     * Returns true if a retry can be attempted.
     * @return  True if retries attempts remain; else false
     */
    public boolean shouldRetry()
    {
        return (numRetries >= 0);
    }

    /**
     * waitUntilNextTry
     * Waits for timeToWaitMS. Ignores any interrupted exception
     */
    public void waitUntilNextTry()
    {
        try {
            Thread.sleep(timeToWaitMS);
        }
        catch (InterruptedException iex) { }
    }

    /**
     * exceptionOccurred
     * Call when an exception has occurred in the block. If the
     * retry limit is exceeded, throws an exception.
     * Else waits for the specified time.
     * @throws Exception
     */
    public void exceptionOccurred() throws Exception
    {
        numRetries--;
        if(!shouldRetry())
        {
            throw new Exception("Network Error");
        }
        waitUntilNextTry();
    }
}

