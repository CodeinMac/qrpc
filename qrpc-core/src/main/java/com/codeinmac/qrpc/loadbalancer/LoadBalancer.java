package com.codeinmac.qrpc.loadbalancer;

import com.codeinmac.qrpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;

/**
 * Load Balancer (used by consumers)
 * <p>
 * Provides load balancing functionality for selecting a service instance from the available list.
 */
public interface LoadBalancer {

    /**
     * Selects a service instance for invocation.
     *
     * @param requestParams       The request parameters
     * @param serviceMetaInfoList The list of available service instances
     * @return The selected service instance meta information
     */
    ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList);
}
