package com.codeinmac.qrpc.serializer;

import java.io.*;

/**
 * JDK serializer that provides personal methods
 * for serializing and deserializing objects
 */
public class JdkSerializer implements Serializer {

    /**
     * serialize the given object to a byte array.
     *
     * @param object
     * @param <T>
     * @return A byte array representing the serialized object.
     * @throws IOException If an I/O error occurs during serialization.
     */
    @Override
    public <T> byte[] serialize(T object) throws IOException {
        // Create a byte array output stream to store the serialized object.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // Create an ObjectOutputStream to write the object to the byte array output stream.
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        objectOutputStream.writeObject(object);
        objectOutputStream.close();
        return outputStream.toByteArray();
    }

    /**
     *  Deserializes the given byte array
     *  to an object of the specified type.
     *
     * @param bytes
     * @param type
     * @param <T>
     * @return The deserialized object.
     * @throws IOException If an I/O error occurs during deserialization.
     */
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        try {
            return (T) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            objectInputStream.close();
        }
    }
}
