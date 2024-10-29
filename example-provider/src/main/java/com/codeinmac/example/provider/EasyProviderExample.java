package com.codeinmac.example.provider;

import com.codeinmac.example.common.service.UserService;
import com.codeinmac.qrpc.registry.LocalRegistry;
import com.codeinmac.qrpc.server.HttpServer;
import com.codeinmac.qrpc.server.VertxHttpServer;

/**
 * Example of a Minimal Service Provider
 */
public class EasyProviderExample {

    public static void main(String[] args) {
        // Register the services
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        // Starting the web service
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(8080);
    }
}
