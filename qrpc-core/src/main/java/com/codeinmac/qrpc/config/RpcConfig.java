package com.codeinmac.qrpc.config;

import com.codeinmac.qrpc.fault.retry.RetryStrategyKeys;
import com.codeinmac.qrpc.fault.tolerant.TolerantStrategyKeys;
import com.codeinmac.qrpc.loadbalancer.LoadBalancerKeys;
import com.codeinmac.qrpc.serializer.SerializerKeys;
import lombok.Builder;
import lombok.Data;

/**
 * Global configuration for the RPC framework.
 */
@Data
public class RpcConfig {

    /**
     * Name of the RPC framework.
     */
    private String name = "qrpc";

    /**
     * Version of the RPC framework.
     */
    private String version = "1.0";

    /**
     * Server hostname.
     */
    private String serverHost = "localhost";

    /**
     * Server port number.
     */
    private Integer serverPort = 8080;

    /**
     * Serializer to use.
     */
    private String serializer = SerializerKeys.JDK;

    /**
     * Load balancer to use.
     */
    private String loadBalancer = LoadBalancerKeys.ROUND_ROBIN;

    /**
     * Retry strategy to use.
     */
    private String retryStrategy = RetryStrategyKeys.NO;

    /**
     * Fault-tolerance strategy to use.
     */
    private String tolerantStrategy = TolerantStrategyKeys.FAIL_FAST;

    /**
     * Enable mock calls.
     */
    private boolean mock = false;

    /**
     * Registry configuration.
     */
    private RegistryConfig registryConfig = new RegistryConfig();
}
