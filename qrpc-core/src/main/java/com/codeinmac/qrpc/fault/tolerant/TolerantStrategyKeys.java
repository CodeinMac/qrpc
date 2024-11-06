package com.codeinmac.qrpc.fault.tolerant;
/**
 * Fault tolerance policy key name constants
 */
public interface TolerantStrategyKeys {

    /**
     * Fault recovery
     */
    String FAIL_BACK = "failBack";

    /**
     * Fast Failure
     */
    String FAIL_FAST = "failFast";

    /**
     * Failover
     */
    String FAIL_OVER = "failOver";

    /**
     * Silent processing
     */
    String FAIL_SAFE = "failSafe";

}
