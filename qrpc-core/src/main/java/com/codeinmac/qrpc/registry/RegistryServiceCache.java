package com.codeinmac.qrpc.registry;

import com.codeinmac.qrpc.model.ServiceMetaInfo;

import java.util.List;

/**
 * Local registry service cache.
 * This class provides a caching mechanism for storing service metadata information locally.
 * It helps improve efficiency by reducing the number of requests to the registry.
 */
public class RegistryServiceCache {

    /**
     * Service cache.
     * Stores the cached service metadata information.
     */
    private List<ServiceMetaInfo> serviceCache;

    /**
     * Write to cache.
     * Stores or updates the service metadata information in the cache.
     *
     * @param newServiceCache The updated list of service metadata.
     */
    public void writeCache(List<ServiceMetaInfo> newServiceCache) {
        this.serviceCache = newServiceCache;
    }

    /**
     * Read from cache.
     * Retrieves the cached service metadata information.
     *
     * @return The list of cached service metadata, or null if no cache exists.
     */
    public List<ServiceMetaInfo> readCache() {
        return this.serviceCache;
    }

    /**
     * Clear cache.
     * Removes the cached service metadata.
     */
    public void clearCache() {
        this.serviceCache = null;
    }
}
