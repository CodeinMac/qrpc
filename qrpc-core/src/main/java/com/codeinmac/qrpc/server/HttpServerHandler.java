package com.codeinmac.qrpc.server;

import com.codeinmac.qrpc.model.RpcRequest;
import com.codeinmac.qrpc.model.RpcResponse;
import com.codeinmac.qrpc.registry.LocalRegistry;
import com.codeinmac.qrpc.serializer.JdkSerializer;
import com.codeinmac.qrpc.serializer.Serializer;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Handler for HTTP requests in the RPC server.
 */
public class HttpServerHandler implements Handler<HttpServerRequest> {

    @Override
    public void handle(HttpServerRequest request) {
        //  Create a serializer to handle serialization
        //  and deserialization
        final Serializer serializer = new JdkSerializer();

        // Log the received request
        System.out.println("Received request: " + request.method() + " " + request.uri());

        // Asynchronously handle the HTTP request body
        request.bodyHandler(body -> {
            byte[] bytes = body.getBytes();
            RpcRequest rpcRequest = null;
            try {
                rpcRequest = serializer.deserialize(bytes, RpcRequest.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Create an RpcResponse object to hold the response.
            RpcResponse rpcResponse = new RpcResponse();
            // If the request is null, set an error message and return.
            if (rpcRequest == null) {
                rpcResponse.setMessage("rpcRequest is null");
                doResponse(request, rpcResponse, serializer);
                return;
            }

            try {
                // Get the implementation class for the requested service
                // from the local registry.
                Class<?> implClass = LocalRegistry.get(rpcRequest.getServiceName());
                Method method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                //Invoke the method on a new instance of the implementation class with the provided arguments.
                Object result = method.invoke(implClass.newInstance(), rpcRequest.getArgs());
                // Set the response data and type.
                rpcResponse.setData(result);
                rpcResponse.setDataType(method.getReturnType());
                rpcResponse.setMessage("ok");
            } catch (Exception e) {
                e.printStackTrace();
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            }
            // Send the response back to the client.
            doResponse(request, rpcResponse, serializer);
        });
    }

    /**
     * Sends the response back to the client.
     *
     * @param request     The HTTP request object.
     * @param rpcResponse The response to be sent.
     * @param serializer  The serializer used to serialize the response.
     */
    void doResponse(HttpServerRequest request, RpcResponse rpcResponse, Serializer serializer) {
        HttpServerResponse httpServerResponse = request.response()
                .putHeader("content-type", "application/json");
        try {
            // Serialize the response object to a byte array.
            byte[] serialized = serializer.serialize(rpcResponse);
            httpServerResponse.end(Buffer.buffer(serialized));
        } catch (IOException e) {
            e.printStackTrace();
            httpServerResponse.end(Buffer.buffer());
            // TODO：add a log here :log.error()....
        }
    }
}
