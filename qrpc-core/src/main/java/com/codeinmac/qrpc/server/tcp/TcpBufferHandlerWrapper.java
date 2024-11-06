package com.codeinmac.qrpc.server.tcp;

import com.codeinmac.qrpc.protocol.ProtocolConstant;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.parsetools.RecordParser;

/**
 * TCP Buffer Handler Wrapper
 * Decorator pattern, enhancing the existing buffer handling capability using RecordParser to solve partial packet and
 * packet concatenation issues.
 */
public class TcpBufferHandlerWrapper implements Handler<Buffer> {

    /**
     * Parser to solve partial packet and packet concatenation issues.
     */
    private final RecordParser recordParser;

    public TcpBufferHandlerWrapper(Handler<Buffer> bufferHandler) {
        recordParser = initRecordParser(bufferHandler);
    }

    @Override
    public void handle(Buffer buffer) {
        recordParser.handle(buffer);
    }

    /**
     * Initializes the parser.
     *
     * @param bufferHandler the handler to process the complete buffer.
     * @return the initialized RecordParser.
     */
    private RecordParser initRecordParser(Handler<Buffer> bufferHandler) {
        // Construct the parser
        RecordParser parser = RecordParser.newFixed(ProtocolConstant.MESSAGE_HEADER_LENGTH);

        parser.setOutput(new Handler<Buffer>() {
            // Initialization
            int size = -1;
            // Complete read (header + body)
            Buffer resultBuffer = Buffer.buffer();

            @Override
            public void handle(Buffer buffer) {
                // 1. In each iteration, read the message header first
                if (-1 == size) {
                    // Read the length of the message body
                    size = buffer.getInt(13);
                    parser.fixedSizeMode(size);
                    // Write header information to the result
                    resultBuffer.appendBuffer(buffer);
                } else {
                    // 2. Then read the message body
                    // Write body information to the result
                    resultBuffer.appendBuffer(buffer);
                    // The buffer is now complete, process it
                    bufferHandler.handle(resultBuffer);
                    // Reset for the next iteration
                    parser.fixedSizeMode(ProtocolConstant.MESSAGE_HEADER_LENGTH);
                    size = -1;
                    resultBuffer = Buffer.buffer();
                }
            }
        });

        return parser;
    }
}
