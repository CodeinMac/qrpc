package com.codeinmac.qrpc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * RPC Response class that represents
 * a response from a remote procedure call.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcResponse implements Serializable {

    /**
     * The data returned from the remote procedure call.
     */
    private Object data;

    /**
     * The type of the response data (reserved for future use)
     */
    private Class<?> dataType;

    /**
     * A message providing additional information about the response
     */
    private String message;

    /**
     *  Any exception that occurred during the remote procedure call
     */
    private Exception exception;

}
