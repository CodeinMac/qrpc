package com.codeinmac.qrpc.fault.retry;

import com.codeinmac.qrpc.model.RpcResponse;
import java.util.concurrent.Callable;

/**
 * retry strategy
 */
public interface RetryStrategy {

    /**
     * retry
     *
     * @param callable
     * @return
     * @throws Exception
     */
    RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception;
}
