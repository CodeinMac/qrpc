package com.codeinmac.qrpc.model;

import cn.hutool.core.util.StrUtil;
import com.codeinmac.qrpc.constant.RpcConstant;
import lombok.Data;

/**
 * Service metadata (registration information).
 */
@Data
public class ServiceMetaInfo {

    /**
     * Service name.
     */
    private String serviceName;

    /**
     * Service version.
     */
    private String serviceVersion = RpcConstant.DEFAULT_SERVICE_VERSION;

    /**
     * Service hostname.
     */
    private String serviceHost;

    /**
     * Service port number.
     */
    private Integer servicePort;

    /**
     * Service group (not implemented yet).
     */
    private String serviceGroup = "default";

    /**
     * Get the service key.
     *
     * @return service key as a string.
     */
    public String getServiceKey() {
        // Can extend to include service group in the future
//        return String.format("%s:%s:%s", serviceName, serviceVersion, serviceGroup);
        return String.format("%s:%s", serviceName, serviceVersion);
    }

    /**
     * Get the service node key for registration.
     *
     * @return service node key as a string.
     */
    public String getServiceNodeKey() {
        return String.format("%s/%s:%s", getServiceKey(), serviceHost, servicePort);
    }

    /**
     * Get the complete service address.
     *
     * @return complete service address as a string.
     */
    public String getServiceAddress() {
        if (!StrUtil.contains(serviceHost, "http")) {
            return String.format("http://%s:%s", serviceHost, servicePort);
        }
        return String.format("%s:%s", serviceHost, servicePort);
    }
}
