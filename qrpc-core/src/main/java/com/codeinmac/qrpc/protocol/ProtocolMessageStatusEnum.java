package com.codeinmac.qrpc.protocol;

import lombok.Getter;

/**
 * Enum representing the status of a protocol message.
 * This enum defines different statuses that can be used in
 * protocol messages to represent
 * different states of requests or responses.
 */
@Getter
public enum ProtocolMessageStatusEnum {

    OK("ok", 20),
    BAD_REQUEST("badRequest", 40),
    BAD_RESPONSE("badResponse", 50);

    // The text representation of the status.
    private final String text;

    // The numeric value representing the status.
    private final int value;

    /**
     * Constructor for initializing the enum values.
     *
     * @param text  The textual description of the status.
     * @param value The numeric value associated with the status.
     */
    ProtocolMessageStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * Retrieves the enum based on the given value.
     * <p>
     * This method allows lookup of the enum using a specific numeric value.
     *
     * @param value The numeric value of the status.
     * @return The corresponding ProtocolMessageStatusEnum, or null if no match is found.
     */
    public static ProtocolMessageStatusEnum getEnumByValue(int value) {
        for (ProtocolMessageStatusEnum anEnum : ProtocolMessageStatusEnum.values()) {
            if (anEnum.value == value) {
                return anEnum;
            }
        }
        return null;
    }
}
