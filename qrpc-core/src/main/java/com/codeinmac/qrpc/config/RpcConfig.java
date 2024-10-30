package com.codeinmac.qrpc.config;

import com.codeinmac.qrpc.serializer.SerializerKeys;
import lombok.Builder;
import lombok.Data;

/**
 * RPC framework configuration
 */
@Data
public class RpcConfig {
    // Name
    private String name;

    // Version number
    private String version;

    //Server hostname
    private String serverHost;

    // Server port number
    private Integer serverPort = 8080;

    // mock test mode
    private boolean mock = false;

    private String serializer = SerializerKeys.JDK;
}
