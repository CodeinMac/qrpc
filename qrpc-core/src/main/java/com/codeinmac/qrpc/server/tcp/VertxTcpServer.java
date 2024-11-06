package com.codeinmac.qrpc.server.tcp;

import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;
import lombok.extern.slf4j.Slf4j;

/**
 * Vert.x TCP Server
 * This class defines a simple TCP server using the Vert.x framework.
 */
@Slf4j
public class VertxTcpServer {

    /**
     * Start the TCP server on the specified port.
     *
     * @param port The port number on which the server will listen for incoming connections.
     */
    public void doStart(int port) {
        // Create a Vert.x instance
        Vertx vertx = Vertx.vertx();

        // Create a TCP server
        NetServer server = vertx.createNetServer();

        // Set up request handler
        server.connectHandler(new TcpServerHandler());

        // Start the TCP server and listen on the specified port
        server.listen(port, result -> {
            if (result.succeeded()) {
                log.info("TCP server started on port " + port);
            } else {
                log.error("Failed to start TCP server: " + result.cause());
            }
        });
    }

    /**
     * Main method to start the server.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        new VertxTcpServer().doStart(8888);
    }
}
