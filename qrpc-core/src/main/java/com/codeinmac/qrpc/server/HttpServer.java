package com.codeinmac.qrpc.server;

public interface HttpServer {

    /**
     * Start the server
     *
     * @param port
     */
    void doStart(int port);
}
