package com.codeinmac.qrpc.fault.tolerant;

import com.codeinmac.qrpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * Degradation to Other Services - Fault Tolerance Policy
 */
@Slf4j
public class FailBackTolerantStrategy implements TolerantStrategy {

    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        // TODO extensible, gets the degraded service and invokes the
        return null;
    }
}
