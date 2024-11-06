package com.codeinmac.qrpc.protocol;

import lombok.Getter;

/**
 * Enum representing the types of protocol messages.
 * <p>
 * This enum defines various types of messages that can be sent in the protocol,
 * such as requests, responses, heartbeat signals, and others.
 */
@Getter
public enum ProtocolMessageTypeEnum {

    REQUEST(0), RESPONSE(1), HEART_BEAT(2), OTHERS(3);

    // The integer value representing the message type.
    private final int key;

    /**
     * Constructor for initializing the enum with its associated key.
     *
     * @param key The integer value representing the message type.
     */
    ProtocolMessageTypeEnum(int key) {
        this.key = key;
    }

    /**
     * Retrieves the enum based on the given key.
     * This method allows lookup of the enum using a specific key value.
     *
     * @param key The integer key of the message type.
     * @return The corresponding ProtocolMessageTypeEnum, or null if no match is found.
     */
    public static ProtocolMessageTypeEnum getEnumByKey(int key) {
        for (ProtocolMessageTypeEnum anEnum : ProtocolMessageTypeEnum.values()) {
            if (anEnum.key == key) {
                return anEnum;
            }
        }
        return null;
    }
}
