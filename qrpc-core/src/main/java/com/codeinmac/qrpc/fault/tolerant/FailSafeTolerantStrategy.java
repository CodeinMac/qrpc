package com.codeinmac.qrpc.fault.tolerant;


import com.codeinmac.qrpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * Silent Handling of Exceptions - Fault Tolerance Policy
 */
@Slf4j
public class FailSafeTolerantStrategy implements TolerantStrategy {

    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        log.info("Handling exceptions silently\n", e);
        return new RpcResponse();
    }
}
