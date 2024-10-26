package com.codeinmac.qrpc.server;

import io.vertx.core.Vertx;

public class VertxHttpServer implements HttpServer {

    /**
     * Start the Server
     *
     * @param port
     */
    public void doStart(int port) {
        // Build Vert.x entry
        Vertx vertx = Vertx.vertx();

        // Build HTTP Server
        io.vertx.core.http.HttpServer server = vertx.createHttpServer();

        // Listening on ports and processing requests
        server.requestHandler(new HttpServerHandler());

        // Start the HTTP server and listen on the specified port.
        server.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("Server is now listening on port " + port);
            } else {
                System.err.println("Failed to start server: " + result.cause());
            }
        });
    }
}
