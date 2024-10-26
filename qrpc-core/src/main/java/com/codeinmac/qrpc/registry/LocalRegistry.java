package com.codeinmac.qrpc.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Local Registration Center that stores service registration information.
 */
public class LocalRegistry {

    /**
     * Storage for registered services,
     * mapping service names to their implementation classes
     */
    private static final Map<String, Class<?>> map = new ConcurrentHashMap<>();

    /**
     * Registers a service by storing its name and implementation class.
     *
     * @param serviceName
     * @param implClass
     */
    public static void register(String serviceName, Class<?> implClass) {
        map.put(serviceName, implClass);
    }

    /**
     * Retrieves the implementation class for a given service name.
     *
     * @param serviceName
     * @return
     */
    public static Class<?> get(String serviceName) {
        return map.get(serviceName);
    }

    /**
     * Removes the registration of a service by its name.
     *
     * @param serviceName
     */
    public static void remove(String serviceName) {
        map.remove(serviceName);
    }
}
