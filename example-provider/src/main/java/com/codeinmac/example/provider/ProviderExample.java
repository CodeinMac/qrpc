package com.codeinmac.example.provider;


import com.codeinmac.example.common.service.UserService;
import com.codeinmac.qrpc.RpcApplication;
import com.codeinmac.qrpc.config.RegistryConfig;
import com.codeinmac.qrpc.config.RpcConfig;
import com.codeinmac.qrpc.model.ServiceMetaInfo;
import com.codeinmac.qrpc.registry.LocalRegistry;
import com.codeinmac.qrpc.registry.Registry;
import com.codeinmac.qrpc.registry.RegistryFactory;
import com.codeinmac.qrpc.server.tcp.VertxTcpServer;

/**
 * Service provider example
 * Demonstrates how to register a service in the RPC framework and start a TCP server.
 * This class includes initialization of the RPC framework, registration of service instances,
 * and the start of a Vertx TCP server to handle incoming requests.
 */
public class ProviderExample {

    public static void main(String[] args) {
        // Initialize the RPC framework
        RpcApplication.init();

        // Register the service locally
        String serviceName = UserService.class.getName();
        LocalRegistry.register(serviceName, UserServiceImpl.class);

        // Register the service in the registry center (e.g., etcd)
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        try {
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Start the TCP server to listen for incoming requests
        VertxTcpServer vertxTcpServer = new VertxTcpServer();
        vertxTcpServer.doStart(8080);
    }
}
