package com.codeinmac.qrpc.loadbalancer;

import com.codeinmac.qrpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Round Robin Load Balancer
 * <p>
 * Implements a load balancing strategy that distributes requests sequentially across available service instances.
 */
public class RoundRobinLoadBalancer implements LoadBalancer {

    /**
     * Index for tracking the current position in the service list.
     */
    private final AtomicInteger currentIndex = new AtomicInteger(0);

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if (serviceMetaInfoList.isEmpty()) {
            return null;
        }
        // If there is only one service, return it directly without load balancing
        int size = serviceMetaInfoList.size();
        if (size == 1) {
            return serviceMetaInfoList.get(0);
        }
        // Use a modulo operation to implement round robin selection
        int index = currentIndex.getAndIncrement() % size;
        return serviceMetaInfoList.get(index);
    }
}
