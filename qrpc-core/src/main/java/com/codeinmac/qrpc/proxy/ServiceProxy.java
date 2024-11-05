package com.codeinmac.qrpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.codeinmac.qrpc.RpcApplication;
import com.codeinmac.qrpc.config.RpcConfig;
import com.codeinmac.qrpc.constant.RpcConstant;
import com.codeinmac.qrpc.model.RpcRequest;
import com.codeinmac.qrpc.model.RpcResponse;
import com.codeinmac.qrpc.model.ServiceMetaInfo;
import com.codeinmac.qrpc.registry.Registry;
import com.codeinmac.qrpc.registry.RegistryFactory;
import com.codeinmac.qrpc.serializer.JdkSerializer;
import com.codeinmac.qrpc.serializer.Serializer;
import com.codeinmac.qrpc.serializer.SerializerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Service Proxy (JDK dynamic proxy).
 * <p>
 * This class implements a dynamic proxy for services, handling the process of creating requests,
 * sending them to the service provider, and deserializing the response.
 */
public class ServiceProxy implements InvocationHandler {

    /**
     * Handles the method invocation on the proxy instance.
     *
     * @param proxy  The proxy instance that the method was invoked on.
     * @param method The Method instance corresponding to the interface method invoked.
     * @param args   The arguments used for the method call.
     * @return The result of the method call, or null if there was an issue.
     * @throws Throwable if any error occurs during the invocation.
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Specify the serializer
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        // Construct the request
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder().serviceName(serviceName).methodName(method.getName()).parameterTypes(method.getParameterTypes()).args(args).build();
        try {
            // Serialize the request
            byte[] bodyBytes = serializer.serialize(rpcRequest);

            // Get the service provider address from the registry
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfoList)) {
                throw new RuntimeException("No service address available");
            }
            ServiceMetaInfo selectedServiceMetaInfo = serviceMetaInfoList.get(0);

            // Send the request
            try (HttpResponse httpResponse = HttpRequest.post(selectedServiceMetaInfo.getServiceAddress()).body(bodyBytes).execute()) {
                byte[] result = httpResponse.bodyBytes();
                // Deserialize the response
                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
                return rpcResponse.getData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}


///**
// * Service Proxy (JDK Dynamic Proxy)
// */
//public class ServiceProxy implements InvocationHandler {
//
//    /**
//     * invoke a proxy (computing)
//     *
//     * @return
//     * @throws Throwable
//     */
//    @Override
//    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        // Specifying the serializer
//        Serializer serializer = new JdkSerializer();
//
//        // constructive request
//        RpcRequest rpcRequest = RpcRequest.builder()
//                .serviceName(method.getDeclaringClass().getName())
//                .methodName(method.getName())
//                .parameterTypes(method.getParameterTypes())
//                .args(args)
//                .build();
//        try {
//            // serialize
//            byte[] bodyBytes = serializer.serialize(rpcRequest);
//            // Send Request
//            // todo 注意，这里地址被硬编码了（需要使用注册中心和服务发现机制解决）
//            try (HttpResponse httpResponse = HttpRequest.post("http://localhost:8080")
//                    .body(bodyBytes)
//                    .execute()) {
//                byte[] result = httpResponse.bodyBytes();
//                // deserialization
//                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
//                return rpcResponse.getData();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//}
