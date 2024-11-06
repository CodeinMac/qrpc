package com.codeinmac.qrpc.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProtocolMessage<T> {

    /**
     * The message header containing metadata.
     */
    private Header header;

    /**
     * The message body, which could be a request or response object.
     */
    private T body;

    /**
     * Inner class representing the protocol message header.
     */
    @Data
    public static class Header {

        /**
         * Magic number, used to ensure protocol security.
         */
        private byte magic;

        /**
         * Protocol version number.
         */
        private byte version;

        /**
         * Serializer type used for serialization.
         */
        private byte serializer;

        /**
         * Type of message (e.g., request or response).
         */
        private byte type;

        /**
         * Status of the message (e.g., success or failure).
         */
        private byte status;

        /**
         * Unique identifier for the request.
         */
        private long requestId;

        /**
         * Length of the message body.
         */
        private int bodyLength;
    }
}