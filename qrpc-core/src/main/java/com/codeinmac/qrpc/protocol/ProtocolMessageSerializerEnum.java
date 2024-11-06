package com.codeinmac.qrpc.protocol;

import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Enum representing the protocol message serializers.
 *
 * This enum defines the available serializers that can
 * be used in the protocol, such as JDK, JSON, Kryo, and Hessian.
 */
@Getter
public enum ProtocolMessageSerializerEnum {

    JDK(0, "jdk"),
    JSON(1, "json"),
    KRYO(2, "kryo"),
    HESSIAN(3, "hessian");

    // The integer value representing the serializer.
    private final int key;

    // The string value representing the serializer type.
    private final String value;

    /**
     * Constructor for initializing the enum with its associated key and value.
     *
     * @param key The integer key representing the serializer.
     * @param value The string representation of the serializer type.
     */
    ProtocolMessageSerializerEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Get a list of all serializer values.
     *
     * This method returns a list of all string values associated with the serializers.
     *
     * @return A list of all serializer values.
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * Retrieve the enum by key.
     *
     * This method returns the corresponding enum instance based on the provided key.
     *
     * @param key The integer key representing the serializer.
     * @return The corresponding ProtocolMessageSerializerEnum, or null if no match is found.
     */
    public static ProtocolMessageSerializerEnum getEnumByKey(int key) {
        for (ProtocolMessageSerializerEnum anEnum : ProtocolMessageSerializerEnum.values()) {
            if (anEnum.key == key) {
                return anEnum;
            }
        }
        return null;
    }

    /**
     * Retrieve the enum by value.
     *
     * This method returns the corresponding enum instance based on the provided value.
     *
     * @param value The string value representing the serializer type.
     * @return The corresponding ProtocolMessageSerializerEnum, or null if the value is empty or no match is found.
     */
    public static ProtocolMessageSerializerEnum getEnumByValue(String value) {
        if (ObjectUtil.isEmpty(value)) {
            return null;
        }
        for (ProtocolMessageSerializerEnum anEnum : ProtocolMessageSerializerEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
}
