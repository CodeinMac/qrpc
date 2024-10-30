package com.codeinmac.qrpc.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Mock Service Proxy (JDK Dynamic Proxy)
 */
@Slf4j
public class MockServiceProxy implements InvocationHandler {

    /**
     * invoke a proxy
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Generate a specific default value object based on the method's return value type
        Class<?> methodReturnType = method.getReturnType();
        log.info("mock invoke {}", method.getName());
        return getDefaultObject(methodReturnType);
    }

    /**
     * Generate a default value object of the specified type
     */
    private Object getDefaultObject(Class<?> type) {
        // base type
        if (type.isPrimitive()) {
            if (type == boolean.class) {
                return false;
            } else if (type == short.class) {
                return (short) 0;
            } else if (type == int.class) {
                return 0;
            } else if (type == long.class) {
                return 0L;
            }
        }
        return null;
    }
}
