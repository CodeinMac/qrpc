package com.codeinmac.example.common.service;

import com.codeinmac.example.common.model.User;

public interface UserService {
    /**
     * get User Info
     */
    User getUser(User user);

    /**
     * To test the return value of the mock interface
     */
    default short getNumber() {
        return 1;
    }
}
