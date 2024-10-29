package com.codeinmac.qrpc.proxy;

import java.lang.reflect.Proxy;

/**
 * Service Proxy Factory (for creating proxy objects)
 */
public class ServiceProxyFactory {

    /**
     * Getting a proxy object based on a service class
     *
     * @param serviceClass
     * @param <T>
     * @return
     */
    public static <T> T getProxy(Class<T> serviceClass) {
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceProxy());
    }
}
