package com.codeinmac.example.consumer;

import com.codeinmac.example.common.model.User;
import com.codeinmac.example.common.service.UserService;
import com.codeinmac.qrpc.config.RpcConfig;
import com.codeinmac.qrpc.proxy.ServiceProxyFactory;
import com.codeinmac.qrpc.utils.ConfigUtils;

/**
 * Example of Service Consumer
 */
public class ConsumerExample {

    public static void main(String[] args) {
        // Get proxy
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("Terminater-800");
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }
        long number = userService.getNumber();
        System.out.println(number);
    }
}