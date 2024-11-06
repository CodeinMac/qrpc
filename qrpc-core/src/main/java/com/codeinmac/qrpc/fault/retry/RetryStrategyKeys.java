package com.codeinmac.qrpc.fault.retry;

/**
 * Retry strategy key name constants
 */
public interface RetryStrategyKeys {

    /**
     * no retry
     */
    String NO = "no";

    /**
     * fixed interval
     */
    String FIXED_INTERVAL = "fixedInterval";

}
