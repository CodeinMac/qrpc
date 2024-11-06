package com.codeinmac.qrpc.server.tcp;

import com.codeinmac.qrpc.model.RpcRequest;
import com.codeinmac.qrpc.model.RpcResponse;
import com.codeinmac.qrpc.protocol.*;
import com.codeinmac.qrpc.registry.LocalRegistry;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * TCP Request Handler
 * This class handles TCP requests from the client by processing RPC requests and sending the appropriate response.
 */
public class TcpServerHandler implements Handler<NetSocket> {

    /**
     * Handles an incoming TCP connection.
     *
     * @param socket the event to handle
     */
    @Override
    public void handle(NetSocket socket) {
        TcpBufferHandlerWrapper bufferHandlerWrapper = new TcpBufferHandlerWrapper(buffer -> {
            // Receive and decode the request
            ProtocolMessage<RpcRequest> protocolMessage;
            try {
                protocolMessage = (ProtocolMessage<RpcRequest>) ProtocolMessageDecoder.decode(buffer);
            } catch (IOException e) {
                throw new RuntimeException("Failed to decode protocol message");
            }

            RpcRequest rpcRequest = protocolMessage.getBody();
            ProtocolMessage.Header header = protocolMessage.getHeader();

            // Process the request
            // Construct the response object
            RpcResponse rpcResponse = new RpcResponse();
            try {
                // Retrieve the service implementation class and invoke the method via reflection
                Class<?> implClass = LocalRegistry.get(rpcRequest.getServiceName());
                Method method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                Object result = method.invoke(implClass.newInstance(), rpcRequest.getArgs());

                // Package the response result
                rpcResponse.setData(result);
                rpcResponse.setDataType(method.getReturnType());
                rpcResponse.setMessage("ok");
            } catch (Exception e) {
                e.printStackTrace();
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            }

            // Send the response after encoding
            header.setType((byte) ProtocolMessageTypeEnum.RESPONSE.getKey());
            header.setStatus((byte) ProtocolMessageStatusEnum.OK.getValue());
            ProtocolMessage<RpcResponse> responseProtocolMessage = new ProtocolMessage<>(header, rpcResponse);
            try {
                Buffer encode = ProtocolMessageEncoder.encode(responseProtocolMessage);
                socket.write(encode);
            } catch (IOException e) {
                throw new RuntimeException("Failed to encode protocol message");
            }
        });

        // Set the handler to process incoming data from the socket
        socket.handler(bufferHandlerWrapper);
    }
}
