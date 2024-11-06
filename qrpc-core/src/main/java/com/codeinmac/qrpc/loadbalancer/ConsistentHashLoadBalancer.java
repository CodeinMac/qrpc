package com.codeinmac.qrpc.loadbalancer;

import com.codeinmac.qrpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Consistent Hash Load Balancer
 * <p>
 * Implements a load balancing strategy using consistent hashing to distribute requests across available service instances.
 *
 * @author <a href="https://github.com/liyupi">coder_yupi</a>
 * @learn <a href="https://codefather.cn">Yupi's Programming Guide</a>
 * @from <a href="https://yupi.icu">Programming Navigation Knowledge Circle</a>
 */
public class ConsistentHashLoadBalancer implements LoadBalancer {

    /**
     * Consistent hash ring to store virtual nodes.
     */
    private final TreeMap<Integer, ServiceMetaInfo> virtualNodes = new TreeMap<>();

    /**
     * Number of virtual nodes per service instance.
     */
    private static final int VIRTUAL_NODE_NUM = 100;

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if (serviceMetaInfoList.isEmpty()) {
            return null;
        }

        // Construct virtual nodes on the hash ring
        for (ServiceMetaInfo serviceMetaInfo : serviceMetaInfoList) {
            for (int i = 0; i < VIRTUAL_NODE_NUM; i++) {
                int hash = getHash(serviceMetaInfo.getServiceAddress() + "#" + i);
                virtualNodes.put(hash, serviceMetaInfo);
            }
        }

        // Get the hash value of the request
        int hash = getHash(requestParams);

        // Select the closest node that is greater than or equal to the request hash value
        Map.Entry<Integer, ServiceMetaInfo> entry = virtualNodes.ceilingEntry(hash);
        if (entry == null) {
            // If no such node exists, wrap around to the beginning of the hash ring
            entry = virtualNodes.firstEntry();
        }
        return entry.getValue();
    }

    /**
     * Hash function to calculate the hash value of a given key.
     *
     * @param key The key to hash.
     * @return The hash value of the key.
     */
    private int getHash(Object key) {
        return key.hashCode();
    }
}
