package com.codeinmac.qrpc.fault.tolerant;

import com.codeinmac.qrpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * Transfer to other service nodes - Fault tolerance policy
 */
@Slf4j
public class FailOverTolerantStrategy implements TolerantStrategy {

    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        // TODO：extensible, to get other service nodes and invoke them
        return null;
    }
}
