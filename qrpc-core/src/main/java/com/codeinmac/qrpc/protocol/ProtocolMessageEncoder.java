package com.codeinmac.qrpc.protocol;

import com.codeinmac.qrpc.serializer.Serializer;
import com.codeinmac.qrpc.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;

/**
 * Protocol Message Encoder
 * This class defines an encoder for protocol messages
 * used in the RPC framework.
 */
public class ProtocolMessageEncoder {

    /**
     * Encodes a ProtocolMessage object into a Buffer.
     *
     * @param protocolMessage The ProtocolMessage to encode.
     * @return A Buffer containing the encoded message.
     * @throws IOException If an error occurs during serialization.
     */
    public static Buffer encode(ProtocolMessage<?> protocolMessage) throws IOException {
        if (protocolMessage == null || protocolMessage.getHeader() == null) {
            return Buffer.buffer();
        }
        ProtocolMessage.Header header = protocolMessage.getHeader();

        // Write bytes to the buffer sequentially
        Buffer buffer = Buffer.buffer();
        buffer.appendByte(header.getMagic());
        buffer.appendByte(header.getVersion());
        buffer.appendByte(header.getSerializer());
        buffer.appendByte(header.getType());
        buffer.appendByte(header.getStatus());
        buffer.appendLong(header.getRequestId());

        // Get the serializer
        ProtocolMessageSerializerEnum serializerEnum = ProtocolMessageSerializerEnum.getEnumByKey(header.getSerializer());
        if (serializerEnum == null) {
            throw new RuntimeException("Serialization protocol does not exist");
        }
        Serializer serializer = SerializerFactory.getInstance(serializerEnum.getValue());

        // Serialize the message body
        byte[] bodyBytes = serializer.serialize(protocolMessage.getBody());

        // Write the body length and data
        buffer.appendInt(bodyBytes.length);
        buffer.appendBytes(bodyBytes);

        return buffer;
    }
}
