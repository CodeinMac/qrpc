package com.codeinmac.qrpc.registry;

import com.codeinmac.qrpc.config.RegistryConfig;
import com.codeinmac.qrpc.model.ServiceMetaInfo;

import java.util.List;

/**
 * Registry interface for the RPC framework.
 */
public interface Registry {

    /**
     * Initialize the registry.
     *
     * @param registryConfig Registry configuration object.
     */
    void init(RegistryConfig registryConfig);

    /**
     * Register a service (server-side).
     *
     * @param serviceMetaInfo Metadata of the service to register.
     */
    void register(ServiceMetaInfo serviceMetaInfo) throws Exception;

    /**
     * Unregister a service (server-side).
     *
     * @param serviceMetaInfo Metadata of the service to unregister.
     */
    void unRegister(ServiceMetaInfo serviceMetaInfo);

    /**
     * Service discovery (retrieve all nodes for a given service, client-side).
     *
     * @param serviceKey Key of the service to discover.
     * @return List of metadata for the discovered service nodes.
     */
    List<ServiceMetaInfo> serviceDiscovery(String serviceKey);

    /**
     * Destroy the registry and release resources.
     */
    void destroy();
}
