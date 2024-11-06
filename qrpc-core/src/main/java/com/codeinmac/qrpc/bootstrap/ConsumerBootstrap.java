package com.codeinmac.qrpc.bootstrap;

import com.codeinmac.qrpc.RpcApplication;

/**
 * Service consumer startup class (initialization)
 */
public class ConsumerBootstrap {

    /**
     * initialization
     */
    public static void init() {
        // RPC framework initialization (configuration and registry)
        RpcApplication.init();
    }
}
