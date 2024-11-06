package com.codeinmac.qrpc.loadbalancer;

import com.codeinmac.qrpc.spi.SpiLoader;

public class LoadBalancerFactory {

    // Load all LoadBalancer implementations using SPI (Service Provider Interface)
    static {
        SpiLoader.load(LoadBalancer.class);
    }

    /**
     * Default load balancer (Round Robin Load Balancer)
     */
    private static final LoadBalancer DEFAULT_LOAD_BALANCER = new RoundRobinLoadBalancer();

    /**
     * Get an instance of the load balancer by key
     *
     * @param key the identifier for the load balancer implementation
     * @return LoadBalancer instance corresponding to the given key
     */
    public static LoadBalancer getInstance(String key) {
        return SpiLoader.getInstance(LoadBalancer.class, key);
    }
}