package com.codeinmac.qrpc.server.tcp;


import cn.hutool.core.util.IdUtil;
import com.codeinmac.qrpc.RpcApplication;
import com.codeinmac.qrpc.model.RpcRequest;
import com.codeinmac.qrpc.model.RpcResponse;
import com.codeinmac.qrpc.model.ServiceMetaInfo;
import com.codeinmac.qrpc.protocol.*;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Vert.x TCP Client for RPC Requests
 * This class defines a client to send TCP requests using Vert.x for handling remote procedure calls (RPC).
 */
public class VertxTcpClient {

    /**
     * Send a request to a remote service.
     *
     * @param rpcRequest      The RPC request object containing the method to invoke and parameters.
     * @param serviceMetaInfo Metadata information of the service to connect to.
     * @return The response from the remote service.
     * @throws InterruptedException If the request is interrupted.
     * @throws ExecutionException   If the request fails to execute.
     */
    public static RpcResponse doRequest(RpcRequest rpcRequest, ServiceMetaInfo serviceMetaInfo) throws InterruptedException, ExecutionException {
        // Send a TCP request
        Vertx vertx = Vertx.vertx();
        NetClient netClient = vertx.createNetClient();
        CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();

        // Connect to the specified service
        netClient.connect(serviceMetaInfo.getServicePort(), serviceMetaInfo.getServiceHost(), result -> {
            if (!result.succeeded()) {
                System.err.println("Failed to connect to TCP server");
                return;
            }
            NetSocket socket = result.result();

            // Build the protocol message
            ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
            ProtocolMessage.Header header = new ProtocolMessage.Header();
            header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
            header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
            header.setSerializer((byte) ProtocolMessageSerializerEnum.getEnumByValue(RpcApplication.getRpcConfig().getSerializer()).getKey());
            header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
            // Generate a unique request ID
            header.setRequestId(IdUtil.getSnowflakeNextId());
            protocolMessage.setHeader(header);
            protocolMessage.setBody(rpcRequest);

            // Encode the request
            try {
                Buffer encodeBuffer = ProtocolMessageEncoder.encode(protocolMessage);
                socket.write(encodeBuffer);
            } catch (IOException e) {
                throw new RuntimeException("Error encoding protocol message", e);
            }

            // Receive the response
            TcpBufferHandlerWrapper bufferHandlerWrapper = new TcpBufferHandlerWrapper(buffer -> {
                try {
                    ProtocolMessage<RpcResponse> rpcResponseProtocolMessage = (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
                    responseFuture.complete(rpcResponseProtocolMessage.getBody());
                } catch (IOException e) {
                    throw new RuntimeException("Error decoding protocol message", e);
                }
            });
            socket.handler(bufferHandlerWrapper);
        });

        // Wait for and get the response
        RpcResponse rpcResponse = responseFuture.get();

        // Close the client connection
        netClient.close();

        return rpcResponse;
    }
}
