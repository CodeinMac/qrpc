package com.codeinmac.qrpc.serializer;

import java.io.IOException;

/**
 * serializer interface
 */
public interface Serializer {

    /**
     * serialize the object
     *
     * @param object
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> byte[] serialize(T object) throws IOException;

    /**
     * deserialize the object
     *
     * @param bytes
     * @param type
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> T deserialize(byte[] bytes, Class<T> type) throws IOException;
}
