package com.codeinmac.qrpc.loadbalancer;

import com.codeinmac.qrpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Random Load Balancer
 * Implements a load balancing strategy that randomly selects a service instance from the available list.
 */
public class RandomLoadBalancer implements LoadBalancer {

    private final Random random = new Random();

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        int size = serviceMetaInfoList.size();
        if (size == 0) {
            return null;
        }
        // If there is only one service, return it directly without random selection
        if (size == 1) {
            return serviceMetaInfoList.get(0);
        }
        // Randomly select a service instance from the list
        return serviceMetaInfoList.get(random.nextInt(size));
    }
}
