package com.codeinmac.qrpc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * RPC Request class that represents
 * a request for a remote procedure call.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequest implements Serializable {

    /**
     * The name of the service to be called.
     */
    private String serviceName;

    /**
     * The name of the method to be invoked on the service.
     */
    private String methodName;

    /**
     * An array of parameter types required by the method.
     */
    private Class<?>[] parameterTypes;

    /**
     * An array of arguments to be passed to the method.
     */
    private Object[] args;

}
