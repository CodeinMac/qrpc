package com.codeinmac.example.consumer;

import com.codeinmac.example.common.model.User;
import com.codeinmac.example.common.service.UserService;
import com.codeinmac.qrpc.proxy.ServiceProxyFactory;

/**
 * Example of a Minimal Service Consumer
 */
public class EasyConsumerExample {

    public static void main(String[] args) {
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("Terminator-800");
        //
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }
    }
}
