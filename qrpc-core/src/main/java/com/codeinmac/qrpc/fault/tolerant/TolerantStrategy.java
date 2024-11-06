package com.codeinmac.qrpc.fault.tolerant;

import com.codeinmac.qrpc.model.RpcResponse;

import java.util.Map;

/**
 * fault tolerance strategy
 */
public interface TolerantStrategy {

    /**
     * error tolerance
     *
     * @param context Context for passing data
     * @param e       exception
     * @return
     */
    RpcResponse doTolerant(Map<String, Object> context, Exception e);
}
