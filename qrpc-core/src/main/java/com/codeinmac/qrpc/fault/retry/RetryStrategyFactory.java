package com.codeinmac.qrpc.fault.retry;


import com.codeinmac.qrpc.spi.SpiLoader;

/**
 * Retry Policy Factory (for getting retryer objects)
 */
public class RetryStrategyFactory {

    static {
        SpiLoader.load(RetryStrategy.class);
    }

    /**
     * Default Retryer
     */
    private static final RetryStrategy DEFAULT_RETRY_STRATEGY = new NoRetryStrategy();

    /**
     * Get Instance
     *
     * @param key
     * @return
     */
    public static RetryStrategy getInstance(String key) {
        return SpiLoader.getInstance(RetryStrategy.class, key);
    }

}
