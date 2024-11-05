package com.codeinmac.qrpc.config;

import com.codeinmac.qrpc.registry.RegistryKeys;
import lombok.Data;

/**
 * RPC framework registry configuration.
 */
@Data
public class RegistryConfig {

    /**
     * Type of registry.
     */
    private String registry = RegistryKeys.ETCD;

    /**
     * Registry address.
     */
    private String address = "http://localhost:2380";

    /**
     * Username for the registry.
     */
    private String username;

    /**
     * Password for the registry.
     */
    private String password;

    /**
     * Timeout duration (in milliseconds).
     */
    private Long timeout = 10000L;
}
