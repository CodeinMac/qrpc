package com.codeinmac.qrpc.fault.tolerant;


import com.codeinmac.qrpc.spi.SpiLoader;

/**
 * Fault Tolerance Policy Factory (factory pattern for getting fault tolerance policy objects)
 */
public class TolerantStrategyFactory {

    static {
        SpiLoader.load(TolerantStrategy.class);
    }

    /**
     * Default Fault Tolerance Policy
     */
    private static final TolerantStrategy DEFAULT_RETRY_STRATEGY = new FailFastTolerantStrategy();

    /**
     * Get Instance
     *
     * @param key
     * @return
     */
    public static TolerantStrategy getInstance(String key) {
        return SpiLoader.getInstance(TolerantStrategy.class, key);
    }

}
