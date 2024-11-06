package com.codeinmac.example.provider;

import com.codeinmac.example.common.service.UserService;
import com.codeinmac.qrpc.bootstrap.ProviderBootstrap;
import com.codeinmac.qrpc.model.ServiceRegisterInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Service provider example
 */
public class ProviderExample {

    public static void main(String[] args) {
        //Services to be registered
        List<ServiceRegisterInfo<?>> serviceRegisterInfoList = new ArrayList<>();
        ServiceRegisterInfo<UserService> serviceRegisterInfo = new ServiceRegisterInfo<>(UserService.class.getName(), UserServiceImpl.class);
        serviceRegisterInfoList.add(serviceRegisterInfo);

        // Service Provider Initialization
        ProviderBootstrap.init(serviceRegisterInfoList);
    }
}
