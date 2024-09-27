package com.codeinmac.example.provider;

import com.codeinmac.example.common.service.UserService;
import com.codeinmac.qrpc.registry.LocalRegistry;
import com.codeinmac.qrpc.server.HttpServer;
import com.codeinmac.qrpc.server.VertxHttpServer;

/**
 * 简易服务提供者示例
 */
public class EasyProviderExample {

    public static void main(String[] args) {
        // 注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        // 启动 web 服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(8080);
    }
}
