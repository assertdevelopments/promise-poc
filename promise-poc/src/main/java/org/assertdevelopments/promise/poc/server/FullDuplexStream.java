/*
 * Copyright 2015 Assert Developments
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.assertdevelopments.promise.poc.server;

import org.assertdevelopments.promise.poc.core.exceptions.HttpStreamException;
import org.assertdevelopments.promise.poc.core.protocol.StreamInputStream;
import org.assertdevelopments.promise.poc.core.protocol.StreamOutputStream;
import org.assertdevelopments.promise.poc.core.protocol.status.StreamStatus;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Implementation for a full duplex stream (reading and writing at the same time).
 *
 * @author Stefan Bangels
 * @see Stream
 * @since 2015-07-10
 */
final class FullDuplexStream implements ClosableStream {

    private final Logger logger = Logger.getLogger(getClass());

    private final StreamInputStream streamInputStream;
    private final StreamOutputStream streamOutputStream;

    /**
     * Default constructor that instantiates a full duplex stream, and wraps it around the provided input- and
     * output stream.
     *
     * @param inputStream  the input stream
     * @param outputStream the output stream
     */
    FullDuplexStream(InputStream inputStream, OutputStream outputStream) {
        this.streamInputStream = new StreamInputStream(inputStream);
        this.streamOutputStream = new StreamOutputStream(outputStream);
    }

    public InputStream getInputStream() {
        return streamInputStream;
    }

    public OutputStream getOutputStream() {
        return streamOutputStream;
    }

    public void finishSuccess() {
        logger.info("sending stream status: success...");
        sendStatus(StreamStatus.STATUS_OK, "OK");
    }

    public void finishError(int code, String message) {
        logger.warn("sending stream status: error (code=" + code + ", message=" + message + ")...");
        sendStatus(code, message);
    }

    /**
     * Finish the stream (read all remaining bytes from the stream input stream, and process the request status) and
     * write the stream status (code and message) to the stream output stream. After calling this method,
     * reading/writing from/to the stream will no longer be possible.
     *
     * @param code    the status code
     * @param message the status message
     */
    private void sendStatus(int code, String message) {
        try {
            // read the remaining bytes of the request and process the request status
            streamInputStream.readRemaining();

            // write eof and status
            streamOutputStream.writeStatus(code, message);
        } catch (IOException e) {
            throw new HttpStreamException("error while sending stream status", e);
        }
    }

}
