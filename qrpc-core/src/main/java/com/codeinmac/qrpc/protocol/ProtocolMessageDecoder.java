package com.codeinmac.qrpc.protocol;

import com.codeinmac.qrpc.model.RpcRequest;
import com.codeinmac.qrpc.model.RpcResponse;
import com.codeinmac.qrpc.serializer.Serializer;
import com.codeinmac.qrpc.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;

/**
 * Protocol Message Decoder
 *
 * This class defines a decoder for protocol messages
 * used in the RPC framework.
 */
public class ProtocolMessageDecoder {

    /**
     * Decodes a Buffer into a ProtocolMessage object.
     *
     * @param buffer The buffer containing the message.
     * @return A ProtocolMessage object decoded from the buffer.
     * @throws IOException If an error occurs during deserialization.
     */
    public static ProtocolMessage<?> decode(Buffer buffer) throws IOException {
        // Read the Buffer at the specified positions
        ProtocolMessage.Header header = new ProtocolMessage.Header();
        byte magic = buffer.getByte(0);

        // Verify the magic number for validity
        if (magic != ProtocolConstant.PROTOCOL_MAGIC) {
            throw new RuntimeException("Invalid magic number in the message");
        }

        header.setMagic(magic);
        header.setVersion(buffer.getByte(1));
        header.setSerializer(buffer.getByte(2));
        header.setType(buffer.getByte(3));
        header.setStatus(buffer.getByte(4));
        header.setRequestId(buffer.getLong(5));
        header.setBodyLength(buffer.getInt(13));

        // Handle the packet length to solve the issue of sticky packets
        byte[] bodyBytes = buffer.getBytes(17, 17 + header.getBodyLength());

        // Parse the message body
        ProtocolMessageSerializerEnum serializerEnum = ProtocolMessageSerializerEnum.getEnumByKey(header.getSerializer());
        if (serializerEnum == null) {
            throw new RuntimeException("Serialization protocol does not exist");
        }

        Serializer serializer = SerializerFactory.getInstance(serializerEnum.getValue());
        ProtocolMessageTypeEnum messageTypeEnum = ProtocolMessageTypeEnum.getEnumByKey(header.getType());
        if (messageTypeEnum == null) {
            throw new RuntimeException("Message type does not exist");
        }

        // Deserialize based on the message type
        switch (messageTypeEnum) {
            case REQUEST:
                RpcRequest request = serializer.deserialize(bodyBytes, RpcRequest.class);
                return new ProtocolMessage<>(header, request);
            case RESPONSE:
                RpcResponse response = serializer.deserialize(bodyBytes, RpcResponse.class);
                return new ProtocolMessage<>(header, response);
            case HEART_BEAT:
            case OTHERS:
            default:
                throw new RuntimeException("Unsupported message type");
        }
    }
}
