package com.codeinmac.qrpc.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.codeinmac.qrpc.model.RpcRequest;
import com.codeinmac.qrpc.model.RpcResponse;
import com.codeinmac.qrpc.serializer.JdkSerializer;
import com.codeinmac.qrpc.serializer.Serializer;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Service Proxy (JDK Dynamic Proxy)
 */
public class ServiceProxy implements InvocationHandler {

    /**
     * invoke a proxy (computing)
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Specifying the serializer
        Serializer serializer = new JdkSerializer();

        // constructive request
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        try {
            // serialize
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            // Send Request
            // todo 注意，这里地址被硬编码了（需要使用注册中心和服务发现机制解决）
            try (HttpResponse httpResponse = HttpRequest.post("http://localhost:8080")
                    .body(bodyBytes)
                    .execute()) {
                byte[] result = httpResponse.bodyBytes();
                // deserialization
                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
                return rpcResponse.getData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
