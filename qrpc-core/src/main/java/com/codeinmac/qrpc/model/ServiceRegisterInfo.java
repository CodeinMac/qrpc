package com.codeinmac.qrpc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRegisterInfo<T> {

    /**
     * Service name
     */
    private String serviceName;

    /**
     * Implementation class
     */
    private Class<? extends T> implClass;
}
