package com.codeinmac.qrpc.registry;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONUtil;
import com.codeinmac.qrpc.config.RegistryConfig;
import com.codeinmac.qrpc.model.ServiceMetaInfo;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;


import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Etcd registry for managing service metadata and registration in an RPC framework.
 * This class provides functionality for initializing an etcd client, registering services,
 * maintaining service leases through periodic heartbeat renewal, and deregistering services.
 * It also supports service discovery and watching for changes in service nodes.
 */
public class EtcdRegistry implements Registry {

    private Client client;

    private KV kvClient;

    /**
     * Root path in etcd for storing service metadata.
     */
    private static final String ETCD_ROOT_PATH = "/rpc/";

    /**
     * Set of registered node keys for local instance (used for maintaining lease renewal).
     */
    private final Set<String> localRegisterNodeKeySet = new HashSet<>();

    /**
     * Initializes the etcd client with the given registry configuration.
     *
     * @param registryConfig Configuration for the registry, including address and timeout settings.
     */
    @Override
    public void init(RegistryConfig registryConfig) {
        client = Client.builder().endpoints(registryConfig.getAddress()).connectTimeout(Duration.ofMillis(registryConfig.getTimeout())).build();
        kvClient = client.getKVClient();
    }

    /**
     * Registers a service in etcd with a lease of 30 seconds.
     *
     * @param serviceMetaInfo Metadata of the service to register.
     * @throws Exception if there is an error during registration.
     */
    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {
        // Create Lease and KV client
        Lease leaseClient = client.getLeaseClient();

        // Create a 30-second lease
        long leaseId = leaseClient.grant(30).get().getID();

        // Set the key-value pair to store
        String registerKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registerKey, StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);

        // Associate key-value pair with lease and set expiry
        PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();
        kvClient.put(key, value, putOption).get();
        // Add node information to local cache
        localRegisterNodeKeySet.add(registerKey);
    }

    /**
     * Unregisters a service from etcd and removes it from the local cache.
     *
     * @param serviceMetaInfo Metadata of the service to unregister.
     */
    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) {
        String registerKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        kvClient.delete(ByteSequence.from(registerKey, StandardCharsets.UTF_8));
        // Also remove from local cache
        localRegisterNodeKeySet.remove(registerKey);
    }

    /**
     * Discovers all available service nodes for a given service key.
     *
     * @param serviceKey Key of the service to discover.
     * @return List of metadata for the discovered service nodes.
     */
    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {
        // Perform a prefix search in etcd
        String searchPrefix = ETCD_ROOT_PATH + serviceKey + "/";

        try {
            // Prefix search in etcd
            GetOption getOption = GetOption.builder().isPrefix(true).build();
            List<KeyValue> keyValues = kvClient.get(ByteSequence.from(searchPrefix, StandardCharsets.UTF_8), getOption).get().getKvs();
            // Parse the service metadata
            return keyValues.stream().map(keyValue -> {
                String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                return JSONUtil.toBean(value, ServiceMetaInfo.class);
            }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to get service list", e);
        }
    }

    /**
     * Destroys the etcd client, deregisters all services, and releases resources.
     * This is typically called when the service is shutting down.
     */
    @Override
    public void destroy() {
        System.out.println("Shutting down current node");
        // Release resources
        if (kvClient != null) {
            kvClient.close();
        }
        if (client != null) {
            client.close();
        }
    }
}
