package com.codeinmac.qrpc;

import com.codeinmac.qrpc.config.RegistryConfig;
import com.codeinmac.qrpc.config.RpcConfig;
import com.codeinmac.qrpc.constant.RpcConstant;
import com.codeinmac.qrpc.registry.Registry;
import com.codeinmac.qrpc.registry.RegistryFactory;
import com.codeinmac.qrpc.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * RPC framework application
 * Equivalent to holder, holds the variables used
 * globally by the project. Double-checked lock
 * singleton pattern implementation
 */
@Slf4j
public class RpcApplication {

    private static volatile RpcConfig rpcConfig;

    /**
     * Initializes the framework with the given configuration.
     *
     * @param newRpcConfig Custom configuration for initializing the RPC framework.
     */
    public static void init(RpcConfig newRpcConfig) {
        rpcConfig = newRpcConfig;
        log.info("rpc init, config = {}", newRpcConfig.toString());
        // Initialize the registry
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        registry.init(registryConfig);
        log.info("registry init, config = {}", registryConfig);
    }

    /**
     * Initialize
     */
    public static void init() {
        RpcConfig newRpcConfig;
        try {
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class,
                    RpcConstant.DEFAULT_CONFIG_PREFIX);
        } catch (Exception e) {
            // Configuration load failed, use default value
            newRpcConfig = new RpcConfig();
            log.error("Configuration load failed, please check your config");
        }
        init(newRpcConfig);
    }


    /**
     * Get config
     */
    public static RpcConfig getRpcConfig() {
        if (rpcConfig == null) {
            synchronized (RpcApplication.class) {
                if (rpcConfig == null) {
                    init();
                }
            }
        }
        return rpcConfig;
    }
}
