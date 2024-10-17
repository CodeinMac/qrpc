package com.codeinmac.qrpc.config;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class RpcConfig {
    //TODO: add comment,
    //TODO: add initial value;
    private String name;

    private String version;

    private String serverHost;

    private Integer serverPort;
}
