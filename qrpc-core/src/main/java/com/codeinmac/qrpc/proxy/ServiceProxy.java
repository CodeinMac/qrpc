package com.codeinmac.qrpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.codeinmac.qrpc.RpcApplication;
import com.codeinmac.qrpc.config.RpcConfig;
import com.codeinmac.qrpc.constant.RpcConstant;
import com.codeinmac.qrpc.fault.retry.RetryStrategy;
import com.codeinmac.qrpc.fault.retry.RetryStrategyFactory;
import com.codeinmac.qrpc.fault.tolerant.TolerantStrategy;
import com.codeinmac.qrpc.fault.tolerant.TolerantStrategyFactory;
import com.codeinmac.qrpc.loadbalancer.LoadBalancer;
import com.codeinmac.qrpc.loadbalancer.LoadBalancerFactory;
import com.codeinmac.qrpc.model.RpcRequest;
import com.codeinmac.qrpc.model.RpcResponse;
import com.codeinmac.qrpc.model.ServiceMetaInfo;
import com.codeinmac.qrpc.registry.Registry;
import com.codeinmac.qrpc.registry.RegistryFactory;
import com.codeinmac.qrpc.serializer.Serializer;
import com.codeinmac.qrpc.serializer.SerializerFactory;
import com.codeinmac.qrpc.server.tcp.VertxTcpClient;


import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service Proxy (JDK Dynamic Proxy)
 * Uses dynamic proxy to handle service calls, including load balancing, retry, and fault tolerance.
 */
public class ServiceProxy implements InvocationHandler {

    /**
     * Invocation handler for dynamic proxy.
     * Handles the method invocation dynamically for remote procedure calls.
     *
     * @param proxy  the proxy instance that the method was invoked on
     * @param method the method instance corresponding to the interface method invoked
     * @param args   the arguments passed to the method invocation
     * @return result of the remote procedure call
     * @throws Throwable in case of an error during method invocation
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Construct the request
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder().serviceName(serviceName).methodName(method.getName()).parameterTypes(method.getParameterTypes()).args(args).build();

        // Retrieve the service provider address from the registry
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
        List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
        if (CollUtil.isEmpty(serviceMetaInfoList)) {
            throw new RuntimeException("No available service address");
        }

        // Load balancing
        LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
        // Use method name (request path) as a parameter for load balancing
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("methodName", rpcRequest.getMethodName());
        ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);

        // Remote procedure call with retry mechanism
        RpcResponse rpcResponse;
        try {
            RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());
            rpcResponse = retryStrategy.doRetry(() -> VertxTcpClient.doRequest(rpcRequest, selectedServiceMetaInfo));
        } catch (Exception e) {
            // Fault tolerance
            TolerantStrategy tolerantStrategy = TolerantStrategyFactory.getInstance(rpcConfig.getTolerantStrategy());
            rpcResponse = tolerantStrategy.doTolerant(null, e);
        }
        return rpcResponse.getData();
    }

    /**
     * Send HTTP request
     *
     * @param selectedServiceMetaInfo the service meta info containing address details
     * @param bodyBytes               the serialized request body
     * @return RpcResponse the response from the service
     * @throws IOException in case of an error during HTTP request execution
     */
    private static RpcResponse doHttpRequest(ServiceMetaInfo selectedServiceMetaInfo, byte[] bodyBytes) throws IOException {
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());
        // Send HTTP request
        try (HttpResponse httpResponse = HttpRequest.post(selectedServiceMetaInfo.getServiceAddress()).body(bodyBytes).execute()) {
            byte[] result = httpResponse.bodyBytes();
            // Deserialize the response
            RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
            return rpcResponse;
        }
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
