package com.codeinmac.qrpc.fault.retry;

import com.codeinmac.qrpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

/**
 * No Retry - Retry Policy
 */
@Slf4j
public class NoRetryStrategy implements RetryStrategy {

    /**
     *
     * @param callable
     * @return
     * @throws Exception
     */
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        return callable.call();
    }
}
