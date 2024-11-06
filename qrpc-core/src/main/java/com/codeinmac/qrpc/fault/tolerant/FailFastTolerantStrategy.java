package com.codeinmac.qrpc.fault.tolerant;



import com.codeinmac.qrpc.model.RpcResponse;
import java.util.Map;

/**
 * Fast Failure - Fault Tolerance Policy (immediate notification to outer caller)
 */
public class FailFastTolerantStrategy implements TolerantStrategy {

    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        throw new RuntimeException("服务报错", e);
    }
}
