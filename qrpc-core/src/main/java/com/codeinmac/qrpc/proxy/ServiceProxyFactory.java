package com.codeinmac.qrpc.proxy;

import com.codeinmac.qrpc.RpcApplication;

import java.lang.reflect.Proxy;

/**
 * Service Proxy Factory (for creating proxy objects)
 */
public class ServiceProxyFactory {

    /**
     * Getting a proxy object based on a service class
     */
    public static <T> T getProxy(Class<T> serviceClass) {
        if (RpcApplication.getRpcConfig().isMock()) {
            return getMockProxy(serviceClass);
        }
        return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(),
                new Class[]{serviceClass}, new ServiceProxy());
    }

    /**
     * Getting Mock Proxy Objects by Service Class
     */
    public static <T> T getMockProxy(Class<T> serviceClass) {
        return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(),
                new Class[]{serviceClass}, new MockServiceProxy());
    }
}
