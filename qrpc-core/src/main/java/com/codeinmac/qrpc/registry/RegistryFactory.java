package com.codeinmac.qrpc.registry;

import com.codeinmac.qrpc.spi.SpiLoader;

/**
 * Registry factory for obtaining registry instances.
 * This class provides methods to retrieve a specific registry instance using the SPI loader mechanism,
 * or to obtain the default registry instance.
 */
public class RegistryFactory {

    static {
        SpiLoader.load(Registry.class);
    }

    /**
     * Default registry instance (EtcdRegistry).
     */
    private static final Registry DEFAULT_REGISTRY = new EtcdRegistry();

    /**
     * Gets an instance of the registry based on the provided key.
     *
     * @param key Key representing the desired registry.
     * @return Registry instance associated with the provided key.
     */
    public static Registry getInstance(String key) {
        return SpiLoader.getInstance(Registry.class, key);
    }

    /**
     * Gets the default registry instance.
     *
     * @return The default registry instance.
     */
    public static Registry getDefaultInstance() {
        return DEFAULT_REGISTRY;
    }
}
