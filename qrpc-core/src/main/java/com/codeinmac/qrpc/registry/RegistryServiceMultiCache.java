package com.codeinmac.qrpc.registry;

import com.codeinmac.qrpc.model.ServiceMetaInfo;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Local registry service cache (supports multiple services).
 * This class implements a local caching mechanism for storing information about multiple services.
 * It allows for faster access and reduces the need for repetitive calls to the registry server.
 */
public class RegistryServiceMultiCache {

    /**
     * Service cache.
     * <p>
     * A map to store service information, where the key is the service name and the value is a list of service metadata.
     */
    Map<String, List<ServiceMetaInfo>> serviceCache = new ConcurrentHashMap<>();

    /**
     * Write to cache.
     * <p>
     * Stores or updates the service metadata information in the cache.
     *
     * @param serviceKey      The key name of the service.
     * @param newServiceCache The updated list of service metadata.
     */
    void writeCache(String serviceKey, List<ServiceMetaInfo> newServiceCache) {
        this.serviceCache.put(serviceKey, newServiceCache);
    }

    /**
     * Read from cache.
     * <p>
     * Retrieves the cached service metadata information for the given service key.
     *
     * @param serviceKey The key name of the service.
     * @return The list of service metadata associated with the service key, or null if not found.
     */
    List<ServiceMetaInfo> readCache(String serviceKey) {
        return this.serviceCache.get(serviceKey);
    }

    /**
     * Clear cache.
     * Removes the service metadata information associated with the given service key from the cache.
     *
     * @param serviceKey The key name of the service.
     */
    void clearCache(String serviceKey) {
        this.serviceCache.remove(serviceKey);
    }
}
