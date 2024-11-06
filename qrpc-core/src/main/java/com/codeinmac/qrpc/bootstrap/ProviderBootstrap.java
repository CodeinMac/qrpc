package com.codeinmac.qrpc.bootstrap;

import com.codeinmac.qrpc.RpcApplication;
import com.codeinmac.qrpc.config.RegistryConfig;
import com.codeinmac.qrpc.config.RpcConfig;
import com.codeinmac.qrpc.model.ServiceMetaInfo;
import com.codeinmac.qrpc.model.ServiceRegisterInfo;
import com.codeinmac.qrpc.registry.LocalRegistry;
import com.codeinmac.qrpc.registry.Registry;
import com.codeinmac.qrpc.registry.RegistryFactory;
import com.codeinmac.qrpc.server.tcp.VertxTcpServer;

import java.util.List;

/**
 * Service Provider Bootstrap Class (Initialization)
 * <p>
 * This class is responsible for initializing the service provider. It sets up
 * the RPC framework, registers the services locally and with the registry center,
 * and starts the TCP server to listen for incoming requests.
 */
public class ProviderBootstrap {

    /**
     * Initialization method
     * <p>
     * This method initializes the service provider by configuring the RPC framework,
     * registering services locally and in the registry center, and starting the TCP server.
     *
     * @param serviceRegisterInfoList List of services to be registered
     */
    public static void init(List<ServiceRegisterInfo<?>> serviceRegisterInfoList) {
        // Initialize the RPC framework (configuration and registry setup)
        RpcApplication.init();
        // Global configuration
        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        // Register services
        for (ServiceRegisterInfo<?> serviceRegisterInfo : serviceRegisterInfoList) {
            String serviceName = serviceRegisterInfo.getServiceName();
            // Local registration
            LocalRegistry.register(serviceName, serviceRegisterInfo.getImplClass());

            // Register service with the registry center
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
            try {
                registry.register(serviceMetaInfo);
            } catch (Exception e) {
                throw new RuntimeException(serviceName + " service registration failed", e);
            }
        }

        // Start the server
        VertxTcpServer vertxTcpServer = new VertxTcpServer();
        vertxTcpServer.doStart(rpcConfig.getServerPort());
    }
}
