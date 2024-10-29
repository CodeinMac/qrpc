package com.codeinmac.example.consumer;

import com.codeinmac.qrpc.config.RpcConfig;
import com.codeinmac.qrpc.utils.ConfigUtils;

/**
 *
 */
public class ConsumerExample {

    public static void main(String[] args) {
        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
        System.out.println(rpc);

    }
}
