package com.codeinmac.qrpc.protocol;

/**
 * Protocol Constants
 */
public class ProtocolConstant {
    /**
     * Length of the message header.
     */
    public static final int MESSAGE_HEADER_LENGTH = 17;

    /**
     * Magic number for the protocol to ensure security.
     */
    public static final byte PROTOCOL_MAGIC = 0x1;

    /**
     * Version number of the protocol.
     */
    public static final byte PROTOCOL_VERSION = 0x1;
}
