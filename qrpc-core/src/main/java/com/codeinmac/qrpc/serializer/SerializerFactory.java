package com.codeinmac.qrpc.serializer;

import com.codeinmac.qrpc.spi.SpiLoader;


/**
 * Serializer factory
 * (factory pattern for getting serializer objects)
 */
public class SerializerFactory {

    /**
     * Serialized mapping (for implementing singleton)
     */
    static {
        SpiLoader.load(Serializer.class);
    }

    /**
     * Default Serializer
     */
    private static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();

    /**
     * Get Instance
     */
    public static Serializer getInstance(String key) {
        return SpiLoader.getInstance(Serializer.class, key);
    }


}
