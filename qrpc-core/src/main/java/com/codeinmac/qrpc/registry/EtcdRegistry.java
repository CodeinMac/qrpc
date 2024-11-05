package com.codeinmac.qrpc.registry;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONUtil;
import com.codeinmac.qrpc.config.RegistryConfig;
import com.codeinmac.qrpc.model.ServiceMetaInfo;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;

import java.nio.charset.StandardCharsets;
import io.etcd.jetcd.watch.WatchEvent;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Etcd Registry Center
 * This class implements a registry based on Etcd, handling the registration,
 * service discovery, and watch mechanisms for RPC services.
 */
public class EtcdRegistry implements Registry {

    private Client client;

    private KV kvClient;

    /**
     * Set of locally registered node keys (used for maintaining lease renewal).
     */
    private final Set<String> localRegisterNodeKeySet = new HashSet<>();

    /**
     * Registry service cache (supports multiple service keys).
     */
    private final RegistryServiceMultiCache registryServiceMultiCache = new RegistryServiceMultiCache();

    /**
     * Set of keys currently being watched.
     */
    private final Set<String> watchingKeySet = new ConcurrentHashSet<>();

    /**
     * Root path in Etcd for service registration.
     */
    private static final String ETCD_ROOT_PATH = "/rpc/";

    @Override
    public void init(RegistryConfig registryConfig) {
        client = Client.builder().endpoints(registryConfig.getAddress()).connectTimeout(Duration.ofMillis(registryConfig.getTimeout())).build();
        kvClient = client.getKVClient();
        heartBeat();
    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {
        // Create Lease and KV client
        Lease leaseClient = client.getLeaseClient();

        // Create a 30-second lease
        long leaseId = leaseClient.grant(30).get().getID();

        // Set key-value pair to store
        String registerKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registerKey, StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);

        // Associate key-value pair with lease and set expiration
        PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();
        kvClient.put(key, value, putOption).get();
        // Add node information to local cache
        localRegisterNodeKeySet.add(registerKey);
    }

    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) {
        String registerKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        kvClient.delete(ByteSequence.from(registerKey, StandardCharsets.UTF_8));
        // Remove from local cache as well
        localRegisterNodeKeySet.remove(registerKey);
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {
        // First try to get the service from the cache
        List<ServiceMetaInfo> cachedServiceMetaInfoList = registryServiceMultiCache.readCache(serviceKey);
        if (cachedServiceMetaInfoList != null) {
            return cachedServiceMetaInfoList;
        }

        // Prefix search, be sure to add '/' at the end
        String searchPrefix = ETCD_ROOT_PATH + serviceKey + "/";

        try {
            // Prefix query
            GetOption getOption = GetOption.builder().isPrefix(true).build();
            List<KeyValue> keyValues = kvClient.get(ByteSequence.from(searchPrefix, StandardCharsets.UTF_8), getOption).get().getKvs();
            // Parse service information
            List<ServiceMetaInfo> serviceMetaInfoList = keyValues.stream().map(keyValue -> {
                String key = keyValue.getKey().toString(StandardCharsets.UTF_8);
                // Watch changes to the key
                watch(key);
                String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                return JSONUtil.toBean(value, ServiceMetaInfo.class);
            }).collect(Collectors.toList());
            // Write service information to cache
            registryServiceMultiCache.writeCache(serviceKey, serviceMetaInfoList);
            return serviceMetaInfoList;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get service list", e);
        }
    }

    @Override
    public void heartBeat() {
        // Renew the lease every 10 seconds
        CronUtil.schedule("*/10 * * * * *", new Task() {
            @Override
            public void execute() {
                // Iterate through all keys for this node
                for (String key : localRegisterNodeKeySet) {
                    try {
                        List<KeyValue> keyValues = kvClient.get(ByteSequence.from(key, StandardCharsets.UTF_8)).get().getKvs();
                        // If the node has expired (needs to be restarted to re-register)
                        if (CollUtil.isEmpty(keyValues)) {
                            continue;
                        }
                        // Node has not expired, re-register (equivalent to renewal)
                        KeyValue keyValue = keyValues.get(0);
                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        ServiceMetaInfo serviceMetaInfo = JSONUtil.toBean(value, ServiceMetaInfo.class);
                        register(serviceMetaInfo);
                    } catch (Exception e) {
                        throw new RuntimeException(key + " lease renewal failed", e);
                    }
                }
            }
        });

        // Support second-level cron tasks
        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }

    /**
     * Watch for changes (client side).
     *
     * @param serviceNodeKey Key of the service node to watch.
     */
    @Override
    public void watch(String serviceNodeKey) {
        Watch watchClient = client.getWatchClient();
        // If not previously watched, start watching
        boolean newWatch = watchingKeySet.add(serviceNodeKey);
        if (newWatch) {
            watchClient.watch(ByteSequence.from(serviceNodeKey, StandardCharsets.UTF_8), response -> {
                for (WatchEvent event : response.getEvents()) {
                    switch (event.getEventType()) {
                        // Triggered when the key is deleted
                        case DELETE:
                            // Clear registered service cache
                            registryServiceMultiCache.clearCache(serviceNodeKey);
                            break;
                        case PUT:
                        default:
                            break;
                    }
                }
            });
        }
    }

    @Override
    public void destroy() {
        System.out.println("Current node offline");
        // Deregister nodes
        for (String key : localRegisterNodeKeySet) {
            try {
                kvClient.delete(ByteSequence.from(key, StandardCharsets.UTF_8)).get();
            } catch (Exception e) {
                throw new RuntimeException(key + " node offline failed");
            }
        }

        // Release resources
        if (kvClient != null) {
            kvClient.close();
        }
        if (client != null) {
            client.close();
        }
    }
}
